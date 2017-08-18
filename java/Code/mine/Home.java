package yzcop

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);
    }
    public void showdcu(View view){
        Intent intent =new Intent(this,googlemap.class);
        startActivity(intent);


    }
    public void serial(View view){
        Intent intent =new Intent(this,SerialCom.class);
        startActivity(intent);


    }


}


