package bennyjon.com.testingapi;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RapidApiConnect connect;
    private EditText smsBody;
    private EditText smsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = new RapidApiConnect("PROJECT", "KEY");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        smsBody = (EditText) findViewById(R.id.smsBody);
        smsNumber = (EditText) findViewById(R.id.smsNumber);
    }

    public void onSendSMSClicked(final View view) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Map<String, Argument> body = new HashMap<>();
                body.put("accountSid", new Argument("data", "SID"));
                body.put("accountToken", new Argument("data", "TOKEN"));
                body.put("from", new Argument("data", "TWILIO_REGITER_NUMBER"));
                body.put("to", new Argument("data", params[0]));
                body.put("body", new Argument("data", params[1]));
                try {
                    Map<String, Object> response = connect.call("Twilio", "sendSms", body);
                    if (response.get("success") != null) {
                        return "MESSAGE SENT";
                    } else {
                        return "ERROR TRYING TO SEND THE MESSAGE.";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }.execute(smsNumber.getText().toString(), smsBody.getText().toString());
    }
}
