package com.example.smstest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private TextView sender;
	private TextView content; 
	private IntentFilter receiverFilter;
	private MessageReceiver messageReceiver;
	private EditText to;
	private EditText msgInput;
	private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * 接收短信
         */
        sender = (TextView) findViewById(R.id.sender);
        content = ((TextView) findViewById(R.id.content));
        receiverFilter = new IntentFilter();
        receiverFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //短信拦截
        receiverFilter.setPriority(100);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, receiverFilter);//对广播进行注册
        /*
         * 发送短信
         */
        to = (EditText) findViewById(R.id.to);
        msgInput = (EditText) findViewById(R.id.msg_input);
        send = (Button) findViewById(R.id.Send);
        send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(to.getText().toString(), null, msgInput.getText().toString(),null, null);
			}
		});
        
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(messageReceiver);//取消对广播的注册
    }
    
    class MessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			Bundle bundle = intent.getExtras();
			Object[]pdus = (Object[]) bundle.get("pdus");//提取短信
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for(int i=0;i<messages.length;i++){
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
			}
			String address = messages[0].getOriginatingAddress();//获取发送方号码
			String fullMessage ="";
			for(SmsMessage message:messages){
				fullMessage += message.getMessageBody();//获取短信内容
			}
			//!!!!!!!!!!!上次就是掉了这两行代码
			sender.setText(address);
			content.setText(fullMessage);
			
			abortBroadcast();
		}
		
		
    	
    }

 
}




















