package com.teste.testewebservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WebServiceTeste extends Activity {

	private EditText sigla;
	private TextView txvretorno;
	private ImageView bandeira;
	private Button carregar;
	private AndroidHttpTransport transporte;
	private SoapObject soapOb;
	private SoapSerializationEnvelope envelope;
	private String NAMESPACE = "http://www.oorsprong.org/websamples.countryinfo";
	private String METHOD_NAME = "CountryFlag";
	private String URL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
	private String SOAP_ACTION = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?op=CountryFlag";
	private String resposta;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_service_teste);

		sigla = (EditText) findViewById(R.id.sigla_ws);
		txvretorno = (TextView) findViewById(R.id.retorno);
		carregar = (Button) findViewById(R.id.btncarregar);
		bandeira = (ImageView) findViewById(R.id.imgbandeira);

		carregar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				soapOb = new SoapObject(NAMESPACE, METHOD_NAME);
				PropertyInfo pi = new PropertyInfo();
				pi.setName("sCountryISOCode");
				pi.setType(String.class);
				pi.setValue(sigla.getText().toString());
				soapOb.addProperty(pi);

				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(soapOb);
				// envelope.dotNet = true;

				transporte = new AndroidHttpTransport(URL);

				try {
					transporte.call(SOAP_ACTION, envelope);
					SoapPrimitive retorno = (SoapPrimitive) envelope
							.getResponse();
					resposta = retorno.toString();
					txvretorno.setText(resposta);
					bandeira.setImageDrawable(geraImgByUrl(retorno.toString()));
					bandeira.setVisibility(0);

				} catch (IOException e) {
					Log.e("Erro WebService", "transporte");
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					Log.e("Erro WebService", "conversao");
					e.printStackTrace();
				}

			}
		});

	}

	private Drawable geraImgByUrl(String url) {

		try {

			InputStream is = (InputStream) new java.net.URL(url).getContent();
			Drawable img = Drawable.createFromStream(is, "src name");
			return img;
		} catch (MalformedURLException e) {
			Log.e("geraImg", e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("geraImg", e.getMessage().toString());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_web_service_teste, menu);
		return true;
	}

}
