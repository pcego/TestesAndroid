package com.teste.testewebservice;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

// Tarefa Assíncrona
public class ConectWebService extends AsyncTask<String, Void, String> {

	private TextView url;
	private Button exec;
	private Button mostrarBandeira;
	private AndroidHttpTransport transporte;
	private SoapObject soapOb;
	private SoapSerializationEnvelope envelope;
	private String NAMESPACE = "http://www.oorsprong.org/websamples.countryinfo";
	private String METHOD_NAME = "CountryFlag";
	private String URL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
	private String SOAP_ACTION = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?op=CountryFlag";
	private String resposta;
	private SoapPrimitive retorno;

	//construtor sobrecarregado
	public ConectWebService(TextView url,Button band, Button exec) {
		super();
		this.url = url;
		this.exec = exec;
		this.mostrarBandeira = band; 
	}

	@Override
	protected void onPostExecute(String result) {

		url.setText(result);
		exec.setEnabled(true);
		mostrarBandeira.setEnabled(true);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		mostrarBandeira.setEnabled(false);
		exec.setEnabled(false);		
		super.onPreExecute();
	}
	
	// método executado em uma Thread a parte
	@Override
	protected String doInBackground(String... params) {
		String sigla = params[0];

		try {
			soapOb = new SoapObject(NAMESPACE, METHOD_NAME);
			PropertyInfo pi = new PropertyInfo();
			pi.setName("sCountryISOCode");
			pi.setType(String.class);
			pi.setValue(sigla);
			soapOb.addProperty(pi);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(soapOb);
			transporte = new AndroidHttpTransport(URL);

			transporte.call(SOAP_ACTION, envelope);
			retorno = (SoapPrimitive) envelope.getResponse();
			resposta = retorno.toString();
			return resposta;
		} catch (IOException e) {
			Log.e("Erro WebService", "transporte");
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			Log.e("Erro WebService", "conversao");
			e.printStackTrace();
		}

		return null;
	}

}
