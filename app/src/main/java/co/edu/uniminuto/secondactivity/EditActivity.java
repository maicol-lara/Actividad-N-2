package co.edu.uniminuto.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    private EditText etEditTask;
    private Button btnUpdate, btnDelete;
    private int position;
    private String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etEditTask = findViewById(R.id.etEditTask);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Obtiene los datos enviados desde MainActivity
        Intent intent = getIntent();
        task = intent.getStringExtra("task");
        position = intent.getIntExtra("position", -1);

        etEditTask.setText(task);

        // Configura los botones de editar y eliminar
        btnUpdate.setOnClickListener(v -> updateTask());
        btnDelete.setOnClickListener(v -> deleteTask());
    }

    // Método para actualizar la tarea
    private void updateTask() {
        String updatedTask = etEditTask.getText().toString().trim();
        if (!updatedTask.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("edited", updatedTask); 
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("deleted", false); 
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    // Método para eliminar la tarea
    private void deleteTask() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", position);
        resultIntent.putExtra("deleted", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
