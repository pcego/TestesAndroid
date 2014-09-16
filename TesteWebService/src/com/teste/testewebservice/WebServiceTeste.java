// pacote do projeto
package com.teste.testewebservice;

// declara��o de importa��es

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// Activity com implementa��o da interface Runnable
public class WebServiceTeste extends Activity implements Runnable {

	// declarando as vari�veis de refer�ncia
	private Thread th; 
	private EditText sigla;
	private TextView txvretorno;
	private ImageView bandeira;
	private Button carregar;
	private Button mostrarBandeira;
	private Handler h;
	private Drawable img;

	// in�cio do ciclo de vida da Activity
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// M�todo que invoca o layout definido para esta Activity atrav�s de
		// seu identificador �nico definido na classe R  
		setContentView(R.layout.activity_web_service_teste);

		//instanciando as Views que comp�em a Activity atrav�s do m�todo
		// findViewById que recebe como parametro o identificador �nico definido na classe R
		sigla = (EditText) findViewById(R.id.sigla_ws);
		txvretorno = (TextView) findViewById(R.id.retorno);
		carregar = (Button) findViewById(R.id.btncarregar);
		bandeira = (ImageView) findViewById(R.id.imgbandeira);
		mostrarBandeira = (Button) findViewById(R.id.btncarregarBandeira);

		// intanciando um Handler (manipulador)
		h = new Handler();

		// atribuindo um ouvinte de click ao bot�o carregar
		carregar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				//instanciando um objeto ConectWebService
				ConectWebService cws = new ConectWebService(txvretorno,
						mostrarBandeira, carregar);

				// chamnado m�todo de execu��o da tarefa assincrona
				cws.execute(sigla.getText().toString());

			}

		});

		mostrarBandeira.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				//instanciando uma nova Thread
				th = new Thread(WebServiceTeste.this);
				//inicializando a Thread
				th.start();

			}
		});

	}

	// m�todo da interface Runnable
	public void run() {

		try {
			//instanciando um ImputStream e buscando o conte�do
			InputStream is = (InputStream) new java.net.URL(txvretorno
					.getText().toString()).getContent();
			
			//criando a imagem recebida 
			img = Drawable.createFromStream(is, "src name");

			//manipulando a view atrav�s do m�todo post
			h.post(new Runnable() {

				public void run() {
					//atribuindo a img ao ImageView
					bandeira.setImageDrawable(img);
					//marcando como vis�vel
					bandeira.setVisibility(0);
				}
			});
			
		// tratando poss�veis exce��es
		} catch (MalformedURLException e) {

			Log.e("Erro Url", "Erro Url IMG", e);
		} catch (IOException e) {

			Log.e("Erro IO", "Erro IMG", e);
		}

	};

}
