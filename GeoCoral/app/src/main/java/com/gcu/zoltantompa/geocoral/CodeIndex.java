package com.gcu.zoltantompa.geocoral;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * code taken and modified from Lab5
 * This class reads a pre-populated database, and displays description about information
 */

public class CodeIndex extends AppCompatActivity {

    boolean debugEnabled = false;

    FragmentManager fmAboutDialogue;

    Intent map_Screen;
    Intent list_Screen;
    Intent settings_Screen;
    Intent codeList_Screen;
    Intent chart_Screen;

    Toast toast;

    List<CodeInstance> codeList;


    //UI variables
    TextView debuggerText;
    TextView typicalValuesTextBox;
    TextView descriptionTextBox;
    Spinner codeListSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_index);

        //setting up new Intents
        map_Screen = new Intent(getApplicationContext(), MapView.class);
        list_Screen = new Intent(getApplicationContext(), ListView.class);
        settings_Screen = new Intent(getApplicationContext(), Settings.class);
        codeList_Screen = new Intent(getApplicationContext(), CodeIndex.class);
        chart_Screen = new Intent(getApplicationContext(), ChartView.class);

        //bind UI
        debuggerText = (TextView)findViewById(R.id.debugTextBox);
        typicalValuesTextBox = (TextView) findViewById(R.id.typicalValuesTextBox);
        descriptionTextBox = (TextView) findViewById(R.id.descriptionTextBox);
        descriptionTextBox.setMovementMethod(new ScrollingMovementMethod());
        codeListSpinner =  (Spinner) findViewById(R.id.codeListSpinner);


        //initDB("sig");
        initSpinner();
    }


    private void initSpinner(){

        //Create database handler instance
        CodeIndexDBMGR codeIndexDBMGR = new CodeIndexDBMGR(this, "dbcodedesc.s3db",null,1);
        try {
            codeIndexDBMGR.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get a list of all the db items
        codeList = codeIndexDBMGR.findAllCodeIndexEntries();
        List<String> spinnerArray =  new ArrayList<String>();

        //get the codes from all the db-items
        for (int i=0; i<codeList.size();i++)
        {
            spinnerArray.add(codeList.get(i).getCode());
        }

        //populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeListSpinner.setAdapter(adapter);

        //set up event listener
        codeListSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if(debugEnabled) {
                            Log.e("debug-on selected", " position: " + position + " id:" + id);
                        }
                        typicalValuesTextBox.setText(codeList.get(position).getTypicalValues());
                        descriptionTextBox.setText(codeList.get(position).getDescription());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
    }



    ///inflating the menu on this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.main_menu, menu);
        return true;
    }

    ///handling the item selection from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_map:
                if(debugEnabled) {
                    System.out.println("Msp option Clicked!");
                    toast = Toast.makeText(getApplicationContext(), "Map option Clicked!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                startActivity(map_Screen);
                finish(); //ending .this activity
                return true;

            case R.id.menu_list:
                if(debugEnabled) {
                    System.out.println("List option Clicked!");
                    toast = Toast.makeText(getApplicationContext(), "List option Clicked!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                startActivity(map_Screen);
                finish(); //ending .this activity
                return true;

            case R.id.menu_codeindex:
                //this is the current option, so ignore
                break;

            case R.id.menu_chart:
                if(debugEnabled) {
                    System.out.println("Chart option Clicked!");
                    toast = Toast.makeText(getApplicationContext(), "Chart option Clicked!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                startActivity(chart_Screen);
                finish(); //ending .this activity
                return true;

            case R.id.menu_settings:
                if(debugEnabled) {
                    System.out.println("Settings option Clicked!");
                    toast = Toast.makeText(getApplicationContext(), "Settings option Clicked!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                startActivity(settings_Screen);
                finish(); //ending .this activity
                return true;

            case R.id.menu_about:
                // About Dialogue;
                DialogFragment AboutDlg = new pcAboutDlg();
                AboutDlg.show(getFragmentManager(), "menu");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }
}
