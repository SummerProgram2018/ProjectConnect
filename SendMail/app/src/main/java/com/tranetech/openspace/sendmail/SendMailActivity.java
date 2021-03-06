package com.tranetech.openspace.sendmail;

/*
Import guide :
Step 1 : Download mail,activation,additionnal jar files and add in your project libs folder in android studio
Files at https://code.google.com/archive/p/javamail-android/downloads
Step 2 : Copy and paste all classes in java folder (mainly GMail and SendMailTask)
Step 3 : Add this to the manifest :
<uses-permission android:name="android.permission.INTERNET"/>
Step 4 : Modify SendMailActivity as necessary, you might want to copy and paste between Start Paste and End Paste
to the script this activity related with
*/

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class SendMailActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button send = (Button) this.findViewById(R.id.button1);

		//START PASTE
		send.setOnClickListener(new View.OnClickListener() {

			//The action to send
			public void onClick(View v) {
				Log.i("SendMailActivity", "Send Button Clicked.");

				/*String fromEmail = ((TextView) findViewById(R.id.editText1))
						.getText().toString();
				String fromPassword = ((TextView) findViewById(R.id.editText2))
						.getText().toString();*/
				String fromEmail = "papillion072018@gmail.com";
				String fromPassword = "dnui2018";

				//Destination email list
				String toEmails = ((TextView) findViewById(R.id.editText3))
						.getText().toString();
				List<String> toEmailList = Arrays.asList(toEmails
						.split("\\s*,\\s*"));
				Log.i("SendMailActivity", "To List: " + toEmailList);
				//Email content (edit as necessary)
				String emailSubject = ((TextView) findViewById(R.id.editText4))
						.getText().toString();
				String emailBody = ((TextView) findViewById(R.id.editText5))
						.getText().toString();
				//MailTask, to send the email
				new SendMailTask(SendMailActivity.this).execute(fromEmail,
						fromPassword, toEmailList, emailSubject, emailBody);
			}
		});
        //END PASTE
	}
}
