package es.vcarmen.chatandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by david on 25/02/17.
 */

public class FragmentoBienvenida extends Fragment {
    EditText nombre, direccion;
    Button boton;
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_bienvenida,container,false);
        nombre= (EditText) view.findViewById(R.id.editextNombre);
        direccion= (EditText) view.findViewById(R.id.editextIP);
        boton= (Button) view.findViewById(R.id.botonBienvenida);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario=nombre.getText().toString();
                String direccionServidor=direccion.getText().toString();
                Bundle bundle=new Bundle();
                bundle.putString("nombre",nombreUsuario);
                bundle.putString("direccion",direccionServidor);
                Fragment fragmento=new FragmentoChat();
                fragmento.setArguments(bundle);
                fm=getFragmentManager();
                ft=fm.beginTransaction();
                ft.hide(FragmentoBienvenida.this);
                ft.addToBackStack("fragmento");
                ft.replace(R.id.contenedor,fragmento);
                ft.commit();

            }
        });

        return view;
    }
}
