package com.example.thegerman.nytimes.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.thegerman.nytimes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterDialog extends DialogFragment {
    Calendar calendar;
    EditText date_picker;
    Spinner spinner;
    Button button;
    CheckBox arts,fashion,sport;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static FilterDialog newInstance(){
        FilterDialog filterDialog = new FilterDialog();

        return filterDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter,null);

        //Settings

        calendar = Calendar.getInstance();
        date_picker = view.findViewById(R.id.editText);
        date_picker.setText(sharedPreferences.getString("date",calendar.getTime().toString()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                formatLabel(date_picker);
            }
        };

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Settings for checkbox

        arts = view.findViewById(R.id.checkBox3);
        fashion = view.findViewById(R.id.checkBox);
        sport = view.findViewById(R.id.checkBox2);

        arts.setChecked(sharedPreferences.getBoolean("arts",false));
        fashion.setChecked(sharedPreferences.getBoolean("fashion",false));
        sport.setChecked(sharedPreferences.getBoolean("sport",false));

        //Settings for spinner
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.deskvalue,android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(sharedPreferences.getInt("sort",0));

        //Settings for Button
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date;
                String order;
                date = date_picker.getText().toString().replace("/","");
                if (spinner.getSelectedItem().toString().equals("Oldest")) {
                    order = "oldest";
                }else {
                    order = "newest";
                }
                MainActivity.requestParams.put("begin_date",date);
                MainActivity.requestParams.put("sort",order);

                String fq ="";
                if(arts.isChecked()){
                    fq = fq + " \"arts\"";
                }
                else{
                    if (fq.contains(" \"arts\"")){
                        fq = fq.replace(" \"arts\"","");
                    }
                }


                if(sport.isChecked()){
                    fq = fq + " \"sports\"";
                }
                else{
                    if (fq.contains(" \"sports\"")){
                        fq = fq.replace(" \"sports\"","");
                    }
                }

                if(fashion.isChecked()){
                    fq = fq + " \"fashion & style\"";
                }
                else{
                    if (fq.contains(" \"fashion & style\"")){
                        fq = fq.replace(" \"fashion & style\"","");
                    }
                }

                if(!TextUtils.isEmpty(fq)){
                    MainActivity.requestParams.put("fq","news_desk:("+fq.trim()+")");
                }

                editor.putBoolean("sport",sport.isChecked());
                editor.putBoolean("fashion",fashion.isChecked());
                editor.putBoolean("arts",arts.isChecked());
                editor.putString("date",date_picker.getText().toString());
                editor.putInt("sort",spinner.getSelectedItemPosition());
                editor.apply();


                dismiss();

            }
        });

        setCancelable(true);

        builder.setView(view);
        Dialog dialog = builder.create();

        return dialog;
    }

    private void formatLabel(EditText editText){
        String format = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

        editText.setText(dateFormat.format(calendar.getTime()));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
