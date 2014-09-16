// pacote do projeto
package com.teste.testewebservice;

// declaração de importações

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

// Activity com implementação da interface Runnable
public class WebServiceTeste extends Activity implements Runnable {

	// declarando as variáveis de referência
	private Thread th; 
	private EditText sigla;
	private TextView txvretorno;
	private ImageView bandeira;
	private Button carregar;
	private Button mostrarBandeira;
	private Handler h;
	private Drawable img;

	// início do ciclo de vida da Activity
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Método que invoca o layout definido para esta Activity através de
		// seu identificador único definido na classe R  
		setContentView(R.layout.activity_web_service_teste);

		//instanciando as Views que compõem a Activity através do método
		// findViewById que recebe como parametro o identificador único definido na classe R
		sigla = (EditText) findViewById(R.id.sigla_ws);
		txvretorno = (TextView) findViewById(R.id.retorno);
		carregar = (Button) findViewById(R.id.btncarregar);
		bandeira = (ImageView) findViewById(R.id.imgbandeira);
		mostrarBandeira = (Button) findViewById(R.id.btncarregarBandeira);

		// intanciando um Handler (manipulador)
		h = new Handler();

		// atribuindo um ouvinte de click ao botão carregar
		carregar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				//instanciando um objeto ConectWebService
				ConectWebService cws = new ConectWebService(txvretorno,
						mostrarBandeira, carregar);

				// chamnado método de execução da tarefa assincrona
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

	// método da interface Runnable
	public void run() {

		try {
			//instanciando um ImputStream e buscando o conteúdo
			InputStream is = (InputStream) new java.net.URL(txvretorno
					.getText().toString()).getContent();
			
			//criando a imagem recebida 
			img = Drawable.createFromStream(is, "src name");

			//manipulando a view através do método post
			h.post(new Runnable() {

				public void run() {
					//atribuindo a img ao ImageView
					bandeira.setImageDrawable(img);
					//marcando como visível
					bandeira.setVisibility(0);
				}
			});
			
		// tratando possíveis exceções
		} catch (MalformedURLException e) {

			Log.e("Erro Url", "Erro Url IMG", e);
		} catch (IOException e) {

			Log.e("Erro IO", "Erro IMG", e);
		}

	};

}
