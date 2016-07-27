package com.huodong.im.chat.service;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.JsonUtil;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ChatService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return new Chat();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	Thread thread;
	private IoSession session = null;
	boolean flag = true;
	final int calCount = 6;
	private static int count_reconnect = 0;


	@Override
	public void onCreate() {
		super.onCreate();
		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {

					while (flag) {
						count_reconnect++;
						try {
							Thread.sleep(1000 * 10);
							if (count_reconnect == calCount) {
								reSetCount();
								if (NetUtil.iSHasNet(ChatService.this)) {
									connect();
								}
							}
						} catch (InterruptedException e) {
						}
					}
				}
			};
			thread.start();
		}

	}

	private static void reSetCount() {
		count_reconnect = 0;
	}

	class Chat extends Binder implements ChatInterface {

		@Override
		public void send(String str) {
			ChatService.this.send(str);
		}

		@Override
		public void connect() {
			ChatService.this.connect();
		}

		@Override
		public void newMessage(int uid,String content) {
			ChatService.this.newMessage(uid,content);

		}

		@Override
		public void closeSession() {
			ChatService.this.closeSession();
		}

		@Override
		public boolean isSessioinConnect() {
			return ChatService.this.isSessioinConnect();
		}

		@Override
		public void reSetCount() {
			ChatService.reSetCount();
		}

	}

	public void closeSession() {
		if (session != null) {
			session.close(false);
		}
	}

	private void newMessage(int uid2, String content) {
		MyApplication yApp = MyApplication.getInstance();
			String str_json = JsonUtil.PackgNewMs(yApp.getUid(), uid2, content);
			send(str_json);
	}

	private void send(String str) {
		try {
			session.write(str);
		} catch (Exception e) {
		}
	}

	private void auth(int uid) {
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.AUTH);
			json.put(UserMode.UID, uid);
			json.put(ChatMode.CONTENT, "I am from client");
			session.write(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean isSessioinConnect() {
		if (session == null) {
			return false;
		}
		return session.isConnected();
	}

	private ChatIoHandler chatIoHandler;
	private static final int IDELTIMEOUT = 30;
	private static final int HEARTBEATRATE = 15;
	private static final String HEARTBEATREQUEST = "1";
	private static final String HEARTBEATRESPONSE = "2";

	public void connect() {
		reSetCount();
		MyApplication yApp = MyApplication.getInstance();
		final int uid = yApp.getUid();
		if (!(uid >0)) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				NioSocketConnector connector = new NioSocketConnector();
				DefaultIoFilterChainBuilder chain = connector.getFilterChain();
				chain.addLast("logger", new LoggingFilter());
				chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
				connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDELTIMEOUT);

				KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
				KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepAliveRequestTimeoutHandlerImpl();
				KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.BOTH_IDLE,
						heartBeatHandler);
				heartBeat.setForwardEvent(true);
				heartBeat.setRequestInterval(HEARTBEATRATE);
				connector.getSessionConfig().setKeepAlive(true);
				connector.getFilterChain().addLast("heartbeat", heartBeat);
				chatIoHandler = new ChatIoHandler(ChatService.this);
				connector.setHandler(chatIoHandler);
				ConnectFuture future = connector
						.connect(new InetSocketAddress(IMConstants.CHAT_HOST, IMConstants.CHAT_PORT));
				future.awaitUninterruptibly();
				try {
					session = future.getSession();
					IoSessionConfig config = session.getConfig();
					config.setReaderIdleTime(Integer.MAX_VALUE);
					MyApplication yApp = MyApplication.getInstance();
					int uid2 = yApp.getUid();
					if (uid == uid2) {
						auth(uid2);
					} else {
						closeSession();
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private static class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

		@Override
		public Object getRequest(IoSession session) {
			return HEARTBEATREQUEST;
		}

		@Override
		public Object getResponse(IoSession session, Object request) {
			return null;
		}

		@Override
		public boolean isRequest(IoSession session, Object message) {
			String str = message.toString().trim();
			boolean isJumpPackage = HEARTBEATREQUEST.equals(str);
			if (isJumpPackage)
				return true;
			return false;
		}

		@Override
		public boolean isResponse(IoSession session, Object message) {
			reSetCount();
			String str = message.toString().trim();
			boolean isJumpPackage = HEARTBEATRESPONSE.equals(str);
			return false;
		}


	}

	private static class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler#
		 * keepAliveRequestTimedOut
		 * (org.apache.mina.filter.keepalive.KeepAliveFilter,
		 * org.apache.mina.core.session.IoSession)
		 */
		@Override
		public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
		}

	}

	@Override
	public void unbindService(ServiceConnection conn) {
		super.unbindService(conn);
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

}
