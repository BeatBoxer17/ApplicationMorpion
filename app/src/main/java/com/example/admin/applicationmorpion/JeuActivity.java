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

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.GetInfoJoueurRequest;
import com.example.admin.applicationmorpion.myrequest.UpdateScoreRequest;

import java.util.ArrayList;
import java.util.Map;

public class JeuActivity extends AppCompatActivity implements View.OnClickListener{

    private RequestQueue queue;
    private SessionManager sessionManager;
    private TextView nom_j1, nom_j2, score1, score2;
    private GetInfoJoueurRequest getInfoJoueurRequest;
    private UpdateScoreRequest updateScoreRequest;
    private Button btn_retour;
    private String id_joueur2 = "";
    private String pseudo_joueur2 = "";
    private String id_joueur1 = "";
    private String pseudo_joueur1 = "";
    private String scorej1 = "0";
    private String scorej2 = "0";
    private Integer couleurj1 = 0;
    public Integer couleurj2 = 0;

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

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        updateScoreRequest = new UpdateScoreRequest(this, queue);
        getInfoJoueurRequest = new GetInfoJoueurRequest(this, queue);
        sessionManager = new SessionManager(this);

        // Si c'est un nouveau duel crée on marque un messsage
        final Intent intent = getIntent();
        if(intent.hasExtra("create")){
            Toast.makeText(this, intent.getStringExtra("create"), Toast.LENGTH_SHORT).show();
        }

        // Récuperation des Element du XML
        nom_j1 = (TextView) findViewById(R.id.joueur1);
        nom_j2 = (TextView) findViewById(R.id.joueur2);
        score1 = (TextView) findViewById(R.id.scoreX);
        score2 = (TextView) findViewById(R.id.scoreO);
        btn_retour = (Button) findViewById(R.id.btn_r_menu);

        // S'il y aun joueur de connecté en session, on recuper son id, son pseudo et sa couleur
        if(sessionManager.isLogged()){
            pseudo_joueur1 = sessionManager.getPseudo();
            id_joueur1 = sessionManager.getId();
            couleurj1 = Integer.valueOf(sessionManager.getIdCouleur());
        }

        // On recupere les donné passer en intent du second joueur, son id et son pseudo
        if(intent.hasExtra("id_joueur2")) {
            id_joueur2 = intent.getStringExtra("id_joueur2");
            pseudo_joueur2 = intent.getStringExtra("pseudo_joueur2");
        }

        // On ajoute les pseudo recuperer au TextViews
        nom_j1.setText(pseudo_joueur1);
        nom_j2.setText(pseudo_joueur2);

        // Requetes pour recuperé le score du joueur 1 dans ce duel
        getInfoJoueurRequest.getInfoJoueur(id_joueur1, id_joueur2, id_joueur1, new GetInfoJoueurRequest.getInfoJoueurCallBack() {
            @Override
            public void onSucces(String score, String couleur) {
                scorej1 = score;
                score1.setText(scorej1);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Requete pour recuperer le score du joueur2 dans ce duel
        getInfoJoueurRequest.getInfoJoueur(id_joueur1, id_joueur2, id_joueur2, new GetInfoJoueurRequest.getInfoJoueurCallBack() {
            @Override
            public void onSucces(String score, String couleur) {
                couleurj2 = Integer.valueOf(couleur);
                scorej2 = score;
                score2.setText(scorej2);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // TextView pour le joueur qui va jouer
        tvJoueur = (TextView) findViewById(R.id.joueur);
        tvJoueur.setText(pseudo_joueur1);

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

        // Quand on clique sur le bouton retour on retourne sur le Menu1.
        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    // Au clic sur une des case du morpion
    public void onClick(View view) {

        // Si personne à cliquer sur ce boutton
        if (view.getBackground() != null) {
            return;
        }

        // On associe a la case le joueur qui a cliquer sur cette case
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
        Drawable drawableJoueur = null;
        // Si le joueur qui doit jouer est X
        if (joueurEnCours == 1) {
            if (couleurj1 == 1){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.rougex);
            }
            if (couleurj1 == 2){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.jaunex);
            }
            if (couleurj1 == 3){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.bleux);
            }
            if (couleurj1 == 4){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.vertx);
            }
            if (couleurj1 == 5){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.noirx);
            }
            if (couleurj1 == 6){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.marronx);
            }
            if (couleurj1 == 7){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.rosex);
            }
            if (couleurj1 == 8){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.violetx);
            }
            if (couleurj1 == 9){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.orangex);
            }
            if (couleurj1 == 10){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.grisx);
            }
        }
        // Sinon c'est O
        else {
            if (couleurj2 == 1){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.rougeo);
            }
            if (couleurj2 == 2){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.jauneo);
            }
            if (couleurj2 == 3){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.bleuo);
            }
            if (couleurj2 == 4){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.verto);
            }
            if (couleurj2 == 5){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.noiro);
            }
            if (couleurj2 == 6){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.marrono);
            }
            if (couleurj2 == 7){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.roseo);
            }
            if (couleurj2 == 8){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.violeto);
            }
            if (couleurj2 == 9){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.orangeo);
            }
            if (couleurj2 == 10){
                drawableJoueur = ContextCompat.getDrawable(this, R.drawable.griso);
            }
        }

        // Mettre le fond au button
        view.setBackgroundDrawable(drawableJoueur);

        // Changement de tour
        // Si le joueur qui doit jouer est X
        if (joueurEnCours == 1) {
            // On met O
            joueurEnCours = 2;
            tvJoueur.setText(pseudo_joueur2);
        }
        // Sinon c'est O
        else {
            // On met X
            joueurEnCours = 1;
            tvJoueur.setText(pseudo_joueur1);
        }

        // Regarder s'il y a un vainqueur
        int res = checkWinner();
        // Si checkWinner retourne 1 c'est joueur1 qui gagner
        if (res == 1){

            TextView ScoreX = (TextView) findViewById(R.id.scoreX);
            int ScoreXint = Integer.valueOf(ScoreX.getText().toString());
            ScoreXint = ScoreXint + 1;
            ScoreX.setText(String.valueOf(ScoreXint));

            // Requete qui ajoute plus 1 au joueur qui à gagner donc joueur1 ici
            updateScoreRequest.updateScore(id_joueur1, id_joueur2, id_joueur1, new UpdateScoreRequest.updateScoreCallBack() {
                @Override
                public void onSucces(String message) {

                }

                @Override
                public void inputErrors(Map<String, String> errors) {

                }

                @Override
                public void onError(String message) {

                }
            });
        } else if (res == 2){ // Si checkWinner retourne 2 c'est  joueur 2 qui a gagner

            TextView ScoreO = (TextView) findViewById(R.id.scoreO);
            int ScoreOint = Integer.valueOf(ScoreO.getText().toString());
            ScoreOint = ScoreOint + 1;
            ScoreO.setText(String.valueOf(ScoreOint));

            // Requete qui ajoute plus 1 au joueur qui à gagner donc joueur2 ici
            updateScoreRequest.updateScore(id_joueur1, id_joueur2, id_joueur2, new UpdateScoreRequest.updateScoreCallBack() {
                @Override
                public void onSucces(String message) {

                }

                @Override
                public void inputErrors(Map<String, String> errors) {

                }

                @Override
                public void onError(String message) {

                }
            });
        } else{ // Sinon on ne fait rien

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
        if (res == 0) { // partie non termine
            return;
        }

        String strToDisplay = "";
        if (res == 1) {
            strToDisplay = pseudo_joueur1 + " à gagné !";
        }
        if (res == 2) {
            strToDisplay = pseudo_joueur2 + " à gagné !";
        }
        if (res == 3) {
            strToDisplay = "Egalité !";
        }

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
