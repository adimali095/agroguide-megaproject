package com.example.recycler.disease_detection;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DiseaseDescHelper
{
    private final String diseaseName;
    private final TextView tvDisease;
    private final TextView tvDiseaseDescription;
    private final TextView tvStepsNeeded;
    private final TextView tvActualSteps;
    private String strDescription,strActualSteps;
    public DiseaseDescHelper(String diseaseName,TextView tvDisease,TextView tvDiseaseDescription,TextView tvStepsNeeded,TextView tvActualSteps)
    {
        //strings
        this.diseaseName = diseaseName;

        //textviews
        this.tvDisease = tvDisease;
        this.tvDiseaseDescription = tvDiseaseDescription;
        this.tvStepsNeeded = tvStepsNeeded;
        this.tvActualSteps = tvActualSteps;
    }
    void getDiseaseDescriptionSteps(String[] disease_description_cure)
    {
        /*
         disease_description_cure hya single string-array mdhe
         sgle disease_name, disease_description, disease_actual_setps defined ahet

         0,3,6,9 indexes na disease_names ahet
         1,4,7,10 indexes na disease_descriptions ahet
         2,5,8,11 indexes nae disease_actual steps ahet
        */
        int i;
        for( i = 0 ;  i < disease_description_cure.length ;  i =  i + 3 )
        {
            if(diseaseName.equals( disease_description_cure[i] ) )
            {
                strDescription = disease_description_cure[i+1];
                strActualSteps = disease_description_cure[i+2];
            }
        }
    }
    void setDiseaseDescriptionSteps()
    {
        //links clickable krnyasathi :
        tvDiseaseDescription.setMovementMethod(LinkMovementMethod.getInstance());
        tvActualSteps.setMovementMethod(LinkMovementMethod.getInstance());


        //Values set krne textviews na :
        tvDisease.setText(diseaseName);
        tvDiseaseDescription.setText(strDescription);
        tvStepsNeeded.setText("Steps Needed");
        tvActualSteps.setText(strActualSteps);
    }
}
