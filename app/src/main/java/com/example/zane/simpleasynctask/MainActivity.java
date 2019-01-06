package com.example.zane.simpleasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

	private static final String TV_KEY = "tv_key";
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv = findViewById(R.id.tv_one);

		if (savedInstanceState != null)
			tv.setText(savedInstanceState.getString(TV_KEY));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(TV_KEY, tv.getText().toString());
	}

	public void startTask(View view) {
		tv.setText(R.string.napping);
		new MyAsyncTask(tv).execute();
	}

	static class MyAsyncTask extends AsyncTask<Void, Void, String>{

		// What is the weak reference (the WeakReference class) for?
		// If you pass a TextView into the AsyncTask constructor and then store it in a member variable,
		// that reference to the TextView means the Activity cannot ever be garbage collected and thus
		// leaks memory, even if the Activity is destroyed and recreated as in a device configuration
		// change. This is called creating a leaky context, and Android Studio will warn you if you try it.

		// The weak reference prevents the memory leak by allowing the object held by that reference
		// to be garbage collected if necessary.

		private WeakReference<TextView> textViewWeakReference;

		MyAsyncTask(TextView textView) {
			this.textViewWeakReference = new WeakReference<>(textView);
		}

		@Override
		protected String doInBackground(Void... voids) {
			Random random = new Random();
			int number = (random.nextInt(11)) * 200;

			try {
				Thread.sleep(number);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "Awake after sleeping for " + number + " milliseconds";
		}

		@Override
		protected void onPostExecute(String s) {
			textViewWeakReference.get().setText(s);
		}
	}
}
