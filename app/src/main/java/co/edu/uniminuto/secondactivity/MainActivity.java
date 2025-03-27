package co.edu.uniminuto.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etTask;
    private EditText etSearch;
    private Button btnAdd;
    private ListView listTask;
    private ArrayList<String> arrayList;
    private ArrayList<String> filteredList;
    private ArrayAdapter<String> adapter;
    private static final int EDIT_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitObject();

        this.btnAdd.setOnClickListener(this::addTask);

        // Establece el listener para cuando se hace clic en un elemento de la lista
        listTask.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedTask = filteredList.get(position);
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("task", selectedTask);
            intent.putExtra("position", arrayList.indexOf(selectedTask));
            startActivityForResult(intent, EDIT_TASK_REQUEST);
        });

        // Establece el listener para el campo de búsqueda
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTasks(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void addTask(View view) {
        String task = this.etTask.getText().toString().trim();
        if (!task.isEmpty()) {
            this.arrayList.add(task);
            filterTasks(etSearch.getText().toString());
            this.etTask.setText("");
        } else {
            Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo para inicializar los elementos de la interfaz de usuario
    private void InitObject() {
        this.etTask = findViewById(R.id.etTask);
        this.etSearch = findViewById(R.id.etSearch);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.listTask = findViewById(R.id.listTask);
        this.arrayList = new ArrayList<>();
        this.filteredList = new ArrayList<>(arrayList);
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.filteredList);
        this.listTask.setAdapter(this.adapter);
    }

    // Metodo para filtrar las tareas
    private void filterTasks(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(arrayList);
        } else {
            for (String task : arrayList) {
                if (task.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(task);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Metodo para manejar los resultados de otras actividades
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK && data != null) {
            String updatedTask = data.getStringExtra("edited");
            int position = data.getIntExtra("position", -1);

            if (position >= 0) {
                if (data.hasExtra("deleted")) {
                    arrayList.remove(position);
                } else if (updatedTask != null) {
                    arrayList.set(position, updatedTask);
                }
                filterTasks(etSearch.getText().toString());
            }
        }
    }
}