package es.vcarmen.chatandroid;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class FragmentoChat extends Fragment {
    WebSocketClient mWebSocketClient;
    EditText editText;
    TextView texto;
    TextView textoEnvio;
    Button boton;
    String nombre;
    String direccion;
    LinearLayout layout;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_chat,container,false);
        editText= (EditText) view.findViewById(R.id.editText);


        //textoEnvio= (TextView) view.findViewById(R.id.textoEnvio);
        boton= (Button) view.findViewById(R.id.enviar);
        nombre=getArguments().getString("nombre");
        direccion=getArguments().getString("direccion").trim();
        connectWebSocket();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().length()>0)
                    enviarMensaje();
            }
        });
        layout= (LinearLayout) view.findViewById(R.id.layoutMensajes);
        scrollView= (ScrollView) view.findViewById(R.id.scrollView);

        return view;
    }
    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://"+direccion+":8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send(nombre+" se ha unido a la conversacion");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(message.contains("se ha unido a la conversacion"))
                            Snackbar.make(getView(),message,Snackbar.LENGTH_LONG).show();
                        else {
                            texto=new TextView(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            if(message.startsWith(nombre.trim())){
                                texto.setGravity(Gravity.END);
                                texto.setText(message.split(": ")[1]);

                            }else{
                                texto.setTypeface(Typeface.DEFAULT_BOLD);
                                texto.setText(message);
                            }

                            texto.setLayoutParams(layoutParams);
                            texto.setTextSize(24);
                            layout.addView(texto);
                           // texto.append(message + "\n");
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.fullScroll(scrollView.FOCUS_DOWN);
                                }
                            });

                        }


                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public void enviarMensaje() {

            mWebSocketClient.send(nombre + ": " + editText.getText().toString());
            textoEnvio = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginEnd(30);
            textoEnvio.setLayoutParams(layoutParams);
            textoEnvio.setTextSize(24);
            textoEnvio.setGravity(Gravity.END);
            textoEnvio.setText(editText.getText().toString());
            layout.addView(textoEnvio);
            editText.setText("");
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.fullScroll(scrollView.FOCUS_DOWN);
                }
            });
        }


}
