package com.example.actividad4b;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private EditText nif,nombre,fechaNac;
    private CheckBox checkEstudiante;
    private RadioButton hombreBut,mujerBut;
    private ImageButton cred,dbAdd;
    private TextView prueba;
    private Context context;
    private Cliente cliente = new Cliente();
    int dia,mes,si;
    boolean validDni=false,validNombre=false,validFechaNac=false,validSexo=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nif = findViewById(R.id.editNIF);
        nombre = findViewById(R.id.editNombre);
        fechaNac = findViewById(R.id.editNac);
        checkEstudiante = findViewById(R.id.checkEstudiante);
        hombreBut = findViewById(R.id.hombreButton);
        mujerBut = findViewById(R.id.mujerButton);
        cred = findViewById(R.id.tarjetaCredito);
        dbAdd = findViewById(R.id.añadirDb);
        prueba = findViewById(R.id.dbout);


        cred.setOnClickListener(view -> {
            Intent intent=new Intent(getBaseContext(),Tarjeta.class);
            startActivity(intent);
        });
        fechaNac.setOnClickListener(v->{
            com.example.actividad4b.DatePicker mDatePickerDialogFragment;
            mDatePickerDialogFragment = new com.example.actividad4b.DatePicker();
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");

        });
        hombreBut.setOnClickListener(v->mujerBut.setChecked(false));
        mujerBut.setOnClickListener(v-> hombreBut.setChecked(false));
        dbAdd.setOnClickListener(v->{
            validarNif();
            validarNombre();
            validarFechaNac();
            validarSexo();
            if (checkEstudiante.isChecked())cliente.setEstudiante(1);
            else cliente.setEstudiante(0);
            if (validDni&&validNombre&&validFechaNac&&validSexo){
                dbthingy(cliente);
                Toast.makeText(this, "Cliente Añadido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dbthingy(Cliente cliente) {
        SpannableStringBuilder resultado = new SpannableStringBuilder("RESULTADO DE LA CONSULTA\n\n");
        resultado.setSpan(new UnderlineSpan(), 0, resultado.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        DbHelper helper = new DbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {

                FileReader fr = new FileReader("clientes.sql");
                BufferedReader br = new BufferedReader(fr);
                db.execSQL(br.readLine());

            Cursor cursor;
            String consult="INSERT INTO clientes ("+cliente.getNif()+","+cliente.getNombre()+","+cliente.getFechanac()+","+cliente;
            cursor = db.rawQuery(consult,null);
            if (cursor.moveToFirst())
                do {
                    resultado.append(cursor.getString(1));
                    resultado.append("\n");
                    resultado.append(cursor.getString(2));
                    resultado.append("\n");
                    resultado.append(String.valueOf(cursor.getInt(3)));
                    resultado.append("\n\n");
                } while (cursor.moveToNext());
            else
                resultado.append("no hay datos");
            cursor.close();
        } catch (SQLiteException e) {
            int l = resultado.length();
            resultado.append("Error: ");
            resultado.append(e.getLocalizedMessage());
            resultado.setSpan(new ForegroundColorSpan(Color.RED), l, resultado.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (IOException e){
            Log.i(null, "onCreate: uh ho io");
        }
        prueba.setText(resultado);
        db.close();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        si=year;
        mCalendar.set(Calendar.MONTH, month);
        mes=month;
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dia=dayOfMonth;
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        fechaNac.setText(selectedDate);
    }
    public void validarNif(){
        try {
       int nums = Integer.parseInt(nif.getText().toString().replaceAll("[^0-9]",""));
       String letra = nif.getText().toString().replaceAll("[^aA-zZ]","");
       nums%=23;
       String[]arr={"t","r","w","a","g","m","y","f","p","d","x","b","n","j","z","s","q","v","h","l","c","k","e"};
        if(arr[nums].equalsIgnoreCase(letra)){
            validDni=true;
            cliente.setNif(nif.getText().toString());
        } else Toast.makeText(this, " NIF no Válido", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            Log.i(null, "validarNif: vacio?");
        }
       }
       
       public void validarNombre(){
            if (!(nombre.getText().toString().equals(""))){
                validNombre=true;
                cliente.setNombre(nombre.getText().toString());
            }else Toast.makeText(this, "Introduce un nombre válido", Toast.LENGTH_SHORT).show();
       }
       public void validarFechaNac(){
            if (!(fechaNac.getText().toString().equals("Pulse dos veces para acceder al calendario"))){
                String [] arr = fechaNac.getText().toString().split(",");
                String fec=si+"-"+mes+"-"+dia;
                validFechaNac=true;
                cliente.setFechanac(fec);
            }else Toast.makeText(this, "Introduce una fecha de nacimiento", Toast.LENGTH_SHORT).show();
       }
       public void validarSexo(){
            if(hombreBut.isChecked()||mujerBut.isChecked()){
                validSexo=true;
                if (hombreBut.isChecked()){
                    cliente.setSexo("Hombre");
                } else cliente.setSexo("Mujer");

            }else Toast.makeText(this, "Selecciona un sexo", Toast.LENGTH_SHORT).show();
       }

    }
