package com.example.actividad4b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Tarjeta extends AppCompatActivity {
    private EditText numero,mes,year,cvc;
    private Button aceptar,cancelar;
    private boolean validNum=false,validMes=false,validAño=false,validCvc=false;
    private ClaseTarjeta clienteTarjeta = new ClaseTarjeta();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);
        numero = findViewById(R.id.numeroTarjeta);
        mes = findViewById(R.id.mesEdit);
        year = findViewById(R.id.añoEdit);
        cvc = findViewById(R.id.cvcEdit);

        numero.setOnClickListener(v-> {
            numero.setText("");
        });
        numero.setOnFocusChangeListener((view, hasfocus) -> {
            if(!hasfocus){
              if(luhn(numero.getText().toString())){
                  numero.setTextColor(Color.parseColor("#00FF00"));
              } else numero.setTextColor(Color.parseColor("#FF0000"));
            }
        });
        mes.setOnClickListener(v-> {
            mes.setText("");

        });
        year.setOnClickListener(v-> {
            year.setText("");

        });
        cvc.setOnClickListener(v-> {
            cvc.setText("");

        });
        aceptar = findViewById(R.id.aceptarButton);
        cancelar = findViewById(R.id.cancelarButton);
        cancelar.setOnClickListener(v->{
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
        });
        aceptar.setOnClickListener(v->{
            validarNumero();
            validarFecha();
            validarCVC();

            if (validNum&&validMes&&validAño&&validCvc){
                Toast.makeText(this, "HUZZA", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static boolean luhn(String value) {
        int sum=0;
        try {
        sum = Character.getNumericValue(value.charAt(value.length() - 1));
        int parity = value.length() % 2;
        for (int i = value.length() - 2; i >= 0; i--) {
            int summand = Character.getNumericValue(value.charAt(i));
            if (i % 2 == parity) {
                int product = summand * 2;
                summand = (product > 9) ? (product - 9) : product;
            }
            sum += summand;
        }
        }catch (StringIndexOutOfBoundsException e){
            Log.i(null, "luhn: mal");
        }
        return (sum % 10) == 0;

    }
    public void validarNumero(){
        if (luhn(numero.getText().toString())){
            validNum=true;
            clienteTarjeta.setNumero(numero.getText().toString());
        }else Toast.makeText(this, "Nº de tarjeta incorrecta", Toast.LENGTH_SHORT).show();
    }
   
    public void validarFecha(){
        try {
            int num= Integer.parseInt(mes.getText().toString().replaceAll("[^0-9]",""));
            if (num>0&&num<13){
                validMes=true;
                clienteTarjeta.setMes(mes.getText().toString());
            }else Toast.makeText(this, "Mes no válido", Toast.LENGTH_SHORT).show();

             num= Integer.parseInt(year.getText().toString().replaceAll("[^0-9]",""));
            if (num>2021&&num<2043){
                validAño=true;
                clienteTarjeta.setAño(year.getText().toString());
            }else Toast.makeText(this, "Año no válido", Toast.LENGTH_SHORT).show();

        }catch (NumberFormatException e){
            Toast.makeText(this, "Meses/años no deben incluir letras", Toast.LENGTH_SHORT).show();
            Log.i(null,"Error de fecha");
        }
        }
    public void validarCVC(){
        try {
        int num = Integer.parseInt(cvc.getText().toString().replaceAll("[^0-9]",""));
        if (String.valueOf(num).length()==3){
            validCvc=true;
            clienteTarjeta.setCVC(cvc.getText().toString());
        }else Toast.makeText(this, "CVC no válido", Toast.LENGTH_SHORT).show();
    }catch (NumberFormatException e){
            Log.i(null, "validarCVC: numeros");
        }
    }
}