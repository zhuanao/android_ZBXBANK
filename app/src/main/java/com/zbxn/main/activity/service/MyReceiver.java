package com.zbxn.main.activity.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zbxn.main.activity.MainActivity;
import com.zbxn.main.activity.login.OutLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by U on 2017/3/8.
 */

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String ModuleId = "";
            String Type = "";
            String Body = "";
            try {
                JSONObject json = new JSONObject(extra);
                Iterator<String> it = json.keys();

                while (it.hasNext()) {
                    String myKey = it.next().toString();
                    Log.d("Main", "结果：[" + myKey + "：" + json.optString(myKey) + "]");
                    if ("ModuleId".equals(myKey)) {
                        ModuleId = json.optString(myKey);
                    } else if ("Type".equals(myKey)) {
                        Type = json.optString(myKey);
                    } else if ("Body".equals(myKey)) {
                        Body = json.optString(myKey);
                    }
                }
            } catch (Exception e) {
                return;
            }

            if ("1".equals(ModuleId) && "-10000".equals(Type)) {
                JPushInterface.clearAllNotifications(context);

                Intent i = new Intent(context, OutLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("description", "您的账号已在其他设备登录");
                context.startActivity(i);
                return;
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            MyToast.showToast("extra:" + extra);

            //测试用
//            Intent intent2 = new Intent(context, PushDetailActivity.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent2.putExtra("Content", extra);
//            context.startActivity(intent2);

            String ModuleId = "";
            String Type = "";
            String Body = "";
            try {
                JSONObject json = new JSONObject(extra);
                Iterator<String> it = json.keys();

                while (it.hasNext()) {
                    String myKey = it.next().toString();
                    Log.d("Main", "结果：[" + myKey + "：" + json.optString(myKey) + "]");
                    if ("ModuleId".equals(myKey)) {
                        ModuleId = json.optString(myKey);
                    } else if ("Type".equals(myKey)) {
                        Type = json.optString(myKey);
                    } else if ("Body".equals(myKey)) {
                        Body = json.optString(myKey);
                    }
                }
            } catch (Exception e) {
                return;
            }
            if ("1".equals(ModuleId) && "-10000".equals(Type)) {
                Intent i = new Intent(context, OutLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("description", "您的账号已在其他设备登录");
                context.startActivity(i);
                return;
            }

            //打开自定义的Activity
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.putExtra("extra", extra);//在main中进行解析
            // intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("index", 0);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent1);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        /*if (Main.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}*/
    }
}
