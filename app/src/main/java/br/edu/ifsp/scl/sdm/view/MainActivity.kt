package br.edu.ifsp.scl.sdm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import br.edu.ifsp.scl.sdm.R
import br.edu.ifsp.scl.sdm.adapter.CursoController
import br.edu.ifsp.scl.sdm.adapter.DisciplinasAdapter
import br.edu.ifsp.scl.sdm.model.Disciplina
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    lateinit var listaDisciplinas: MutableList<Disciplina>
    lateinit var disciplinasAdapter: DisciplinasAdapter
    lateinit var cursoController: CursoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lista disciplinas do adapter
        listaDisciplinas = mutableListOf()
        disciplinasAdapter = DisciplinasAdapter(this, R.layout.disciplina_item, listaDisciplinas)

        // Setando o adapter do ListView
        disciplinasLv.adapter = disciplinasAdapter

        // Criando controller e solicitando atualizacao de lista
        cursoController = CursoController(this)

        // Inserindo disciplinas falsas
        insereDisciplinasFalsas()

        // Solicitando todas as disciplinas para o controller
        cursoController.buscaTodas()

        // Registra o ListView para conter menu decontexto
        registerForContextMenu(disciplinasLv)

        //Setando listener de clique nos itens
        disciplinasLv.setOnItemClickListener(this)
    }

    private fun insereDisciplinasFalsas() {
        for (i in 1..50) {
            val d = Disciplina("D${i}", "Disciplina ${i}", "Ementa ${i}")
            cursoController.insereDisciplina(d)
        }
    }

    fun atualizaLista(listaAtualizada: List<Disciplina>) {
        // Limpa list anterior
        listaDisciplinas.clear()
        // Adiciona os elementos retornados pelo Controler
        listaDisciplinas.addAll(listaAtualizada)
        // Notifica adapter das mudancas na sua fonte de dados
        disciplinasAdapter.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu_contexto, menu)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val disciplina = listaDisciplinas[position]

        // Criando AlertDialog pra mostrar a ementa da disciplina
        val ementaDialog = with(AlertDialog.Builder(this)) {
            setTitle(disciplina.codigo)
            setMessage("${disciplina.nome} :\n ${disciplina.ementa}")
            create()
        }
        ementaDialog.show()
    }

}
