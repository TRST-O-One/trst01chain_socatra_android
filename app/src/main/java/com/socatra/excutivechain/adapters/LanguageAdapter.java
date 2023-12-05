package com.socatra.excutivechain.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.AppLanguageHDRTable;

import java.util.List;
import java.util.Objects;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    List<AppLanguageHDRTable> languageList;
    Context context;

    SharedPreferences sharedPreferences;

    String selectedLanguage;
    String language;

    Dialog dialog;
    LanguageCallbackInterface languageCallbackInterface;

    public LanguageAdapter(List<AppLanguageHDRTable> languageList, SharedPreferences sharedPreferences,
                           String selectedLanguage,Dialog dialog,LanguageCallbackInterface languageCallbackInterface) {
        this.languageList = languageList;
        this.sharedPreferences = sharedPreferences;
        this.selectedLanguage = selectedLanguage;
        this.dialog=dialog;
        this.languageCallbackInterface=languageCallbackInterface;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new LanguageViewHolder(LayoutInflater.from(context).inflate(R.layout.dialog_language_selection_individual,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.txtLang.setText(languageList.get(position).getLanguageName());
        String temp=getLanguageCap(languageList.get(position).getLanguageName().trim());
        Log.e("LangAdpt",temp);

        if (Objects.equals(selectedLanguage, getLanguageCap(languageList.get(position).getLanguageName().trim()))){
            Log.e("LangAdpt",selectedLanguage);
            holder.layoutMain.setBackgroundResource(R.drawable.bg_ui_light_orange_box_border);
            Log.e("LangAdpt","1st if here");
        } else if (selectedLanguage==null){
            if (getLanguageCap(languageList.get(position).getLanguageName().trim())=="English"){
                holder.layoutMain.setBackgroundResource(R.drawable.bg_ui_light_orange_box_border);
                Log.e("LangAdpt","2nd if here");
            }
        } else {
            Log.e("LangAdpt",selectedLanguage);
            Log.e("LangAdpt","else here");
        }
        holder.layoutMain.setOnClickListener(v -> {
            language=getLanguageCap(languageList.get(position).getLanguageName());
            sharedPreferences.edit().putString("selected_language", language).apply();
            Toast.makeText(context,language+" Saved!!", Toast.LENGTH_SHORT).show();
            languageCallbackInterface.languageCallback(position,languageList.get(position));
            dialog.dismiss();
        });

    }

    private String getLanguageCap(String word) {
        word=word.toLowerCase().trim();
        word=Character.toUpperCase(word.charAt(0)) + word.substring(1);
        return word;
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView txtLang;
        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutMain=itemView.findViewById(R.id.laySelectLang);
            txtLang=itemView.findViewById(R.id.txtSelectLangInd);
        }
    }

    public interface LanguageCallbackInterface {
        void languageCallback(int position, AppLanguageHDRTable appLanguageHDRTable);
    }
}
