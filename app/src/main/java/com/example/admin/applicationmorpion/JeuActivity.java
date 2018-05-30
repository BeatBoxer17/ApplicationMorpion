package com.example.admin.applicationmorpion;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JeuActivity extends AppCompatActivity implements View.OnClickListener{

    // Definition des variable
    // Tableau à deux dimension plateau[colonne][ligne]
    private int plateau[][] = new int[3][3];

    // 1 : X
    // 2 : O
    private int joueurEnCours = 1;

    // TextView pour afficher le joueur qui doit jouer
    private TextView tvJoueur;

    // Collection de tout les boutons
    private ArrayList<Button> all_buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);

        Intent intent = getIntent();
        if(intent.hasExtra("create")){
            Toast.makeText(this, intent.getStringExtra("create"), Toast.LENGTH_SHORT).show();
        }

        // TextView pour le joueur qui va jouer
        tvJoueur = (TextView) findViewById(R.id.joueur);

        // Boutton / Case
        Button bt1 = (Button) findViewById(R.id.bt1);
        Button bt2 = (Button) findViewById(R.id.bt2);
        Button bt3 = (Button) findViewById(R.id.bt3);
        Button bt4 = (Button) findViewById(R.id.bt4);
        Button bt5 = (Button) findViewById(R.id.bt5);
        Button bt6 = (Button) findViewById(R.id.bt6);
        Button bt7 = (Button) findViewById(R.id.bt7);
        Button bt8 = (Button) findViewById(R.id.bt8);
        Button bt9 = (Button) findViewById(R.id.bt9);

        // Ajouter les boutton
        all_buttons.add(bt1);
        all_buttons.add(bt2);
        all_buttons.add(bt3);
        all_buttons.add(bt4);
        all_buttons.add(bt5);
        all_buttons.add(bt6);
        all_buttons.add(bt7);
        all_buttons.add(bt8);
        all_buttons.add(bt9);

        // Pour chaque boutton
        for (Button bt : all_buttons){
            // Pas de couleur de fond
            bt.setBackgroundDrawable(null);
            // Quand on clique dessus
            bt.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View view) {

        // Si personne à cliquer sur ce boutton
        if (view.getBackground() != null) {
            return;
        }

        // Si view.getId == R.id.bt1
        switch (view.getId()) {
            case R.id.bt1:
                plateau[0][0] = joueurEnCours;
                break;
            case R.id.bt2:
                plateau[1][0] = joueurEnCours;
                break;
            case R.id.bt3:
                plateau[2][0] = joueurEnCours;
                break;
            case R.id.bt4:
                plateau[0][1] = joueurEnCours;
                break;
            case R.id.bt5:
                plateau[1][1] = joueurEnCours;
                break;
            case R.id.bt6:
                plateau[2][1] = joueurEnCours;
                break;
            case R.id.bt7:
                plateau[0][2] = joueurEnCours;
                break;
            case R.id.bt8:
                plateau[1][2] = joueurEnCours;
                break;
            case R.id.bt9:
                plateau[2][2] = joueurEnCours;
                break;
            default:
                return;
        }

        // image a mettre
        Drawable drawableJoueur;
        // Si le joueur qui doit jouer est X
        if (joueurEnCours == 1) {
            drawableJoueur = ContextCompat.getDrawable(this, R.drawable.x);
        }
        // Sinon c'est O
        else {
            drawableJoueur = ContextCompat.getDrawable(this, R.drawable.o);
        }

        // Mettre le fond au button
        view.setBackgroundDrawable(drawableJoueur);

        // Changement de tour
        // Si le joueur qui doit jouer est X
        if (joueurEnCours == 1) {
            // On met O
            joueurEnCours = 2;
            tvJoueur.setText("O");
        }
        // Sinon c'est O
        else {
            // On met X
            joueurEnCours = 1;
            tvJoueur.setText("X");
        }

        // Regarder s'il y a un vainqueur
        int res = checkWinner();
        if (res == 1){
            TextView ScoreX = (TextView) findViewById(R.id.scoreX);
            int ScoreXint = Integer.valueOf(ScoreX.getText().toString());
            ScoreXint = ScoreXint + 1;
            ScoreX.setText(String.valueOf(ScoreXint));
        } else if (res == 2){
            TextView ScoreO = (TextView) findViewById(R.id.scoreO);
            int ScoreOint = Integer.valueOf(ScoreO.getText().toString());
            ScoreOint = ScoreOint + 1;
            ScoreO.setText(String.valueOf(ScoreOint));
        } else{

        }

        // Afficher qui à gagner ou s'il y a égalité
        displayAlertDialog(res);
    }

    private int checkWinner(){

        // on regarde si il y a un gagnant sur les colonnes
        for (int col = 0; col <= 2; col++) {
            if (plateau[col][0] != 0 && plateau[col][0] == plateau[col][1] && plateau[col][0] == plateau[col][2]) {
                return plateau[col][0];
            }
        }

        // on regarde si il y a un gagnant sur les lignes
        for (int line = 0; line <= 2; line++){
            if (plateau[0][line] != 0 && plateau[0][line] == plateau[1][line] && plateau[0][line] == plateau[2][line]) {
                return plateau[0][line];
            }
        }

        // on regarde si il y a un gagnant sur la diagonale haut/gauche -> bas/droit
        if (plateau[0][0] != 0 && plateau[0][0] == plateau[1][1] && plateau[0][0] == plateau[2][2]) {
            return plateau[0][0];
        }

        // on regarde si il y a un gagnant sur la diagonale haut/droite -> bas/gauche
        if (plateau[2][0] != 0 && plateau[2][0] == plateau[1][1] && plateau[2][0] == plateau[0][2]) {
            return plateau[2][0];
        }

        // Egalité
        boolean isPlateauPlein = true;
        for (int col = 0; col <= 2; col++) {
            for (int line = 0; line <= 2; line++){
                if (plateau[col][line] == 0) { // case
                    isPlateauPlein = false;
                    break;
                }
            }
            if (!isPlateauPlein) {
                break;
            }
        }
        if (isPlateauPlein) {
            return 3;
        }

        // Partie non fini
        return 0;
    }

    // 0 : partie non fini
    // 1 : X
    // 2 : O
    // 3 : egalite
    private void displayAlertDialog(int res){
        if (res == 0) // partie non termine
            return;

        String strToDisplay = "";
        if (res == 1)
            strToDisplay = "Le joueur X à gagné !";
        if (res == 2)
            strToDisplay = "Le joueur O à gagné !";
        if (res == 3)
            strToDisplay = "Egalité !";

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Fin de la partie");
        alertDialog.setMessage(strToDisplay);

        alertDialog.setNeutralButton("Recommencer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetGame();
            }
        });

        // Pour empecher retour sur le téléphone
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void resetGame(){

        for (int col = 0; col <= 2; col++) {
            for (int line = 0; line <= 2; line++) {
                plateau[col][line] = 0;
            }
        }

        for (Button bt : all_buttons) {
            bt.setBackgroundDrawable(null);
        }
    }
}
