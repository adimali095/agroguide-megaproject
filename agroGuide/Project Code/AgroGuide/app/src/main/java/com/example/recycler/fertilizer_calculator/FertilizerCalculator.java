package com.example.recycler.fertilizer_calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.R;

import java.text.DecimalFormat;

public class FertilizerCalculator extends AppCompatActivity {


    //get edittext objects from fertilizer bag values
    private EditText et_N_val;
    private EditText et_P_val;
    private EditText et_K_val;

    //get edittext objects for other values
    private EditText et_N_amt;
    private EditText et_tot_area;


    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_calculator);


        //get inputs from spinner objects for area units


        Button calc_N = findViewById(R.id.calc_N);
        Button calc_P = findViewById(R.id.calc_P);
        Button calc_K = findViewById(R.id.calc_K);


        //sets the edit texts to the right fields
        et_N_val = findViewById(R.id.n_val);
        et_P_val = findViewById(R.id.p_val);
        et_K_val = findViewById(R.id.k_val);
        et_N_amt = findViewById(R.id.n_amt);
        et_tot_area = findViewById(R.id.area_num);

        //sets the result area text view
        tv_result = findViewById(R.id.Result);

        calc_N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCalculation_N();
            }
        });
        calc_K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCalculation_K();
            }
        });
        calc_P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCalculation_P();
            }
        });
    }

    private boolean isEmpty(EditText et1) {
        return et1.getText().toString().trim().length() == 0;
    }

    private void makeCalculation_N() {


        String sp1, sp2;
        Spinner sp_spinner1 = findViewById(R.id.spinner1);
        Spinner sp_spinner2 = findViewById(R.id.spinner2);

        sp1 = sp_spinner1.getSelectedItem().toString();
        sp2 = sp_spinner2.getSelectedItem().toString();

        //create vars to hold the input values
        double N_val, P_val, K_val;
        String spinner1result, spinner2result;

        //N_amt is amount of nitrogen per area (ex: 1000ft2, acre...etc)
        //lbofN is how much fert (in lbs) needed to have 1 lb of nitrogen
        double N_amt, lbofN, lbofP, lbofK, areaInput;

        //for the end amount of each
        double N_result, P_result, K_result, T_result;
        N_result = P_result = K_result = T_result = 0.0;

        String result = "";

        boolean n_inp, p_inp, k_inp, amt_inp, area_inp;

        n_inp = isEmpty(et_N_val);
        p_inp = isEmpty(et_P_val);
        k_inp = isEmpty(et_K_val);
        area_inp = isEmpty(et_tot_area);
        amt_inp = isEmpty(et_N_amt);

        boolean err = false;

        if (!(n_inp) & !(p_inp) & !(k_inp) & !(area_inp) & !(amt_inp) & !(err)) {

            N_val = ((Integer.parseInt(et_N_val.getText().toString())) / 100.0);
            P_val = ((Integer.parseInt(et_P_val.getText().toString())) / 100.0);
            K_val = ((Integer.parseInt(et_K_val.getText().toString())) / 100.0);
            N_amt = Double.parseDouble(et_N_amt.getText().toString());
            areaInput = Double.parseDouble(et_tot_area.getText().toString());


            //Create decimal format so output is only 2 places
            DecimalFormat df = new DecimalFormat("#.##");
            //do the calculations


            lbofN = (1.0 / N_val);
            lbofP = (1.0 / P_val);
            lbofK = (1.0 / K_val);
            if (sp1.equals("1000 ft^2") && sp2.equals("ft^2")) {
                //only if they input per 1000ft2
                T_result = ((lbofN * N_amt) * (areaInput / 1000.0));
                P_result = (T_result / lbofP);
                K_result = (T_result / lbofK);
                N_result = (T_result / lbofN);

            } else if (sp1.equals("Acre") && sp2.equals("Acre(s)")) {
                T_result = ((lbofN * N_amt) * (areaInput));
                P_result = (T_result / lbofP);
                K_result = (T_result / lbofK);
                N_result = (T_result / lbofN);

            } else if (sp1.equals("1000 ft^2") && sp2.equals("Acre(s)")) {
                T_result = ((lbofN * N_amt) * (areaInput * (43560.0 / 1000.0)));
                P_result = (T_result / lbofP);
                K_result = (T_result / lbofK);
                N_result = (T_result / lbofN);

            } else if (sp1.equals("Acre") && sp2.equals("ft^2")) {
                T_result = ((lbofN * N_amt) * (areaInput * (1.0 / 43560.0)));
                P_result = (T_result / lbofP);
                K_result = (T_result / lbofK);
                N_result = (T_result / lbofN);

            }

            result = "Fertilizer needed: " + df.format(T_result) + " pounds\n   " + "Total N applied: " + df.format(N_result) + " pounds\n   This also applies:\n   " +
                    df.format(P_result) + " pounds of P\n   " + df.format(K_result) + " pounds of K.";

            tv_result.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv_result.setText(result);
        } else err = true;
        if (err) {
            if (n_inp) {
                EditText edText = findViewById(R.id.n_val);
                edText.setHintTextColor(Color.RED);
            }
            if (p_inp) {
                EditText edText = findViewById(R.id.p_val);
                edText.setHintTextColor(Color.RED);
            }
            if (k_inp) {
                EditText edText = findViewById(R.id.k_val);
                edText.setHintTextColor(Color.RED);
            }
            if (area_inp) {
                EditText edText = findViewById(R.id.area_num);
                edText.setHintTextColor(Color.RED);
            }
            if (amt_inp) {
                EditText edText = findViewById(R.id.n_amt);
                edText.setHintTextColor(Color.RED);
            }
            Toast.makeText(FertilizerCalculator.this,
                    "Please fill out highlighted fields", Toast.LENGTH_LONG).show();

        }


    }

    private void makeCalculation_P() {


        String sp1, sp2;
        Spinner sp_spinner1 = findViewById(R.id.spinner1);
        Spinner sp_spinner2 = findViewById(R.id.spinner2);

        sp1 = sp_spinner1.getSelectedItem().toString();
        sp2 = sp_spinner2.getSelectedItem().toString();

        String result = "";

        //create vars to hold the input values
        double N_val, P_val, K_val;

        //N_amt is amount of nitrogen per area (ex: 1000ft2, acre...etc)
        //lbofN is how much fert (in lbs) needed to have 1 lb of nitrogen
        double P_amt, lbofN, lbofP, lbofK, areaInput;

        //for the end amount of each
        double N_result, P_result, K_result, T_result;
        N_result = P_result = K_result = T_result = 0.0;

        boolean n_inp, p_inp, k_inp, amt_inp, area_inp;

        n_inp = isEmpty(et_N_val);
        p_inp = isEmpty(et_P_val);
        k_inp = isEmpty(et_K_val);
        area_inp = isEmpty(et_tot_area);
        amt_inp = isEmpty(et_N_amt);

        boolean err = false;

        if (!(n_inp) & !(p_inp) & !(k_inp) & !(area_inp) & !(amt_inp) & !(err)) {

            N_val = (Integer.parseInt(et_N_val.getText().toString()) / 100.0);
            P_val = (Integer.parseInt(et_P_val.getText().toString()) / 100.0);
            K_val = (Integer.parseInt(et_K_val.getText().toString()) / 100.0);
            P_amt = Double.parseDouble(et_N_amt.getText().toString());
            areaInput = Double.parseDouble(et_tot_area.getText().toString());

            lbofN = (1.0 / N_val);
            lbofP = (1.0 / P_val);
            lbofK = (1.0 / K_val);

            //Create decimal format so output is only 2 places
            DecimalFormat df = new DecimalFormat("#.##");
            if (sp1.equals("1000 ft^2") && sp2.equals("ft^2")) {
                //only if they input per 1000ft2
                T_result = ((lbofP * P_amt) * (areaInput / 1000.0));
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);
                P_result = (T_result / lbofP);

            } else if (sp1.equals("Acre") && sp2.equals("Acre(s)")) {
                T_result = ((lbofP * P_amt) * (areaInput));
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);
                P_result = (T_result / lbofP);

            } else if (sp1.equals("1000 ft^2") && sp2.equals("Acre(s)")) {
                T_result = ((lbofP * P_amt) * (areaInput * (43560.0 / 1000.0)));
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);
                P_result = (T_result / lbofP);

            } else if (sp1.equals("Acre") && sp2.equals("ft^2")) {
                T_result = ((lbofP * P_amt) * (areaInput * (1.0 / 43560.0)));
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);
                P_result = (T_result / lbofP);

            }

            result = "Fertilizer needed: " + df.format(T_result) + " pounds\n   " + "Total P applied: " + df.format(P_result) + " pounds\n   This also applies:\n   " +
                    df.format(N_result) + " pounds of N\n   " + df.format(K_result) + " pounds of K.";
            tv_result.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv_result.setText(result);
        } else err = true;
        if (err) {
            if (n_inp) {
                EditText edText = findViewById(R.id.n_val);
                edText.setHintTextColor(Color.RED);
            }
            if (p_inp) {
                EditText edText = findViewById(R.id.p_val);
                edText.setHintTextColor(Color.RED);
            }
            if (k_inp) {
                EditText edText = findViewById(R.id.k_val);
                edText.setHintTextColor(Color.RED);
            }
            if (area_inp) {
                EditText edText = findViewById(R.id.area_num);
                edText.setHintTextColor(Color.RED);
            }
            if (amt_inp) {
                EditText edText = findViewById(R.id.n_amt);
                edText.setHintTextColor(Color.RED);
            }
            Toast.makeText(FertilizerCalculator.this,
                    "Please fill out highlighted fields", Toast.LENGTH_LONG).show();

        }


    }

    private void makeCalculation_K() {


        String sp1, sp2;
        Spinner sp_spinner1 = findViewById(R.id.spinner1);
        Spinner sp_spinner2 = findViewById(R.id.spinner2);

        sp1 = sp_spinner1.getSelectedItem().toString();
        sp2 = sp_spinner2.getSelectedItem().toString();

        String result = "";
        //create vars to hold the input values
        double N_val, P_val, K_val;

        //N_amt is amount of nitrogen per area (ex: 1000ft2, acre...etc)
        //lbofN is how much fert (in lbs) needed to have 1 lb of nitrogen
        double K_amt, lbofN, lbofP, lbofK, areaInput;

        //for the end amount of each
        double N_result, P_result, K_result, T_result;
        N_result = P_result = K_result = T_result = 0.0;

        boolean n_inp, p_inp, k_inp, amt_inp, area_inp;

        n_inp = isEmpty(et_N_val);
        p_inp = isEmpty(et_P_val);
        k_inp = isEmpty(et_K_val);
        area_inp = isEmpty(et_tot_area);
        amt_inp = isEmpty(et_N_amt);

        boolean err = false;

        if (!(n_inp) & !(p_inp) & !(k_inp) & !(area_inp) & !(amt_inp) & !(err)) {

            N_val = (Integer.parseInt(et_N_val.getText().toString()) / 100.0);
            P_val = (Integer.parseInt(et_P_val.getText().toString()) / 100.0);
            K_val = (Integer.parseInt(et_K_val.getText().toString()) / 100.0);
            K_amt = Double.parseDouble(et_N_amt.getText().toString());
            areaInput = Double.parseDouble(et_tot_area.getText().toString());

            lbofN = (1.0 / N_val);
            lbofP = (1.0 / P_val);
            lbofK = (1.0 / K_val);

            //Create decimal format so output is only 2 places
            DecimalFormat df = new DecimalFormat("#.##");
            if (sp1.equals("1000 ft^2") && sp2.equals("ft^2")) {
                //only if they input per 1000ft2
                T_result = ((lbofK * K_amt) * (areaInput / 1000.0));
                P_result = (T_result / lbofP);
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);

            } else if (sp1.equals("Acre") && sp2.equals("Acre(s)")) {
                T_result = ((lbofK * K_amt) * (areaInput));
                P_result = (T_result / lbofP);
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);

            } else if (sp1.equals("1000 ft^2") && sp2.equals("Acre(s)")) {
                T_result = ((lbofK * K_amt) * (areaInput * (43560.0 / 1000.0)));
                P_result = (T_result / lbofP);
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);

            } else if (sp1.equals("Acre") && sp2.equals("ft^2")) {
                T_result = ((lbofK * K_amt) * (areaInput * (1.0 / 43560.0)));
                P_result = (T_result / lbofP);
                N_result = (T_result / lbofN);
                K_result = (T_result / lbofK);
            }

            result = "Fertilizer needed: " + df.format(T_result) + " pounds\n   " + "Total K applied: " + df.format(K_result) + " pounds\n   This also applies:\n   " +
                    df.format(P_result) + " pounds of P\n   " + df.format(N_result) + " pounds of N.";

            tv_result.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv_result.setText(result);


        } else err = true;
        if (err) {
            if (n_inp) {
                EditText edText = findViewById(R.id.n_val);
                edText.setHintTextColor(Color.RED);
            }
            if (p_inp) {
                EditText edText = findViewById(R.id.p_val);
                edText.setHintTextColor(Color.RED);
            }
            if (k_inp) {
                EditText edText = findViewById(R.id.k_val);
                edText.setHintTextColor(Color.RED);
            }
            if (area_inp) {
                EditText edText = findViewById(R.id.area_num);
                edText.setHintTextColor(Color.RED);
            }
            if (amt_inp) {
                EditText edText = findViewById(R.id.n_amt);
                edText.setHintTextColor(Color.RED);
            }
            Toast.makeText(FertilizerCalculator.this,
                    "Please fill out highlighted fields", Toast.LENGTH_LONG).show();

        }
    }
}
