package com.teste.testewebservice;

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

public class WebServiceTeste extends Activity implements Runnable {

	private Thread th;
	private EditText sigla;
	private TextView txvretorno;
	private ImageView bandeira;
	private Button carregar;
	private Button mostrarBandeira;
	private Handler h;
	private Drawable img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_service_teste);

		sigla = (EditText) findViewById(R.id.sigla_ws);
		txvretorno = (TextView) findViewById(R.id.retorno);
		carregar = (Button) findViewById(R.id.btncarregar);
		bandeira = (ImageView) findViewById(R.id.imgbandeira);
		mostrarBandeira = (Button) findViewById(R.id.btncarregarBandeira);

		h = new Handler();

		carregar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				ConectWebService cws = new ConectWebService(txvretorno,
						mostrarBandeira, carregar);

				cws.execute(sigla.getText().toString());

			}

		});

		mostrarBandeira.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				th = new Thread(WebServiceTeste.this);
				th.start();

			}
		});

	}

	public void run() {

		try {
			
			InputStream is = (InputStream) new java.net.URL(txvretorno
					.getText().toString()).getContent();
			img = Drawable.createFromStream(is, "src name");

			h.post(new Runnable() {

				public void run() {
					bandeira.setImageDrawable(img);
					bandeira.setVisibility(0);
				}
			});

		} catch (MalformedURLException e) {

			Log.e("Erro Url", "Erro Url IMG", e);
		} catch (IOException e) {

			Log.e("Erro IO", "Erro IMG", e);
		}

	};

}
