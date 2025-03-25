package com.adso.proyectosemperparatus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GestionProductos extends AppCompatActivity {

    EditText edtCodigo, edtNombreProducto, edtPrecio;
    Button btnBuscar, btnAgregar, btnEditar, btnEliminar, btnSalir;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_productos);

        edtCodigo=(EditText)findViewById(R.id.edtCodigo);
        edtNombreProducto=(EditText)findViewById(R.id.edtNombreProducto);
        edtPrecio=(EditText)findViewById(R.id.edtPrecio);
        btnBuscar=(Button) findViewById(R.id.btnBuscar);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnEditar=(Button)findViewById(R.id.btnEditar);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);
        btnSalir=(Button)findViewById(R.id.btnSalir);

        btnAgregar.setOnClickListener((v)-> {
            ejecutarServicio("http://192.168.137.1/dvcolibri/insertar_producto.php");
        });

        btnBuscar.setOnClickListener((v)-> {
            buscarProducto("http://192.168.137.1/dvcolibri/buscar_producto.php?idProducto="+edtCodigo.getText());
            ;
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarServicio("http://192.168.137.1/dvcolibri/editar_producto.php");

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarProducto("http://192.168.137.1/dvcolibri/eliminar_producto.php");

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void ejecutarServicio(String URL) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idProducto",edtCodigo.getText().toString());
                parametros.put("nombreProducto",edtNombreProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i=0;i<response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtNombreProducto.setText(jsonObject.getString("nombreProducto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    void eliminarProducto(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "EL PRODUCTO FUE ELIMINADO", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("idProducto", edtCodigo.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void limpiarFormulario(){
        edtCodigo.setText("");
        edtNombreProducto.setText("");
        edtPrecio.setText("");
    }
}