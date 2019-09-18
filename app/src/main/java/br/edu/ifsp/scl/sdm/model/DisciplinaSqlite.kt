package br.edu.ifsp.scl.sdm.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.scl.sdm.R
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.ATRIBUTO_CODIGO
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.ATRIBUTO_EMENTA
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.ATRIBUTO_NOME
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.CREATE_TABLE_STM
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.CURSO_BD
import br.edu.ifsp.scl.sdm.model.DisciplinaSqlite.Constantes.TABELA_DISCIPLINA
import java.sql.SQLException

class DisciplinaSqlite(contexto: Context) : DisciplinaDao{

    object Constantes {
        val CURSO_BD = "curso"
        val TABELA_DISCIPLINA = "disciplina"
        val ATRIBUTO_CODIGO = "codigo"
        val ATRIBUTO_NOME = "nome"
        val ATRIBUTO_EMENTA = "ementa"
        val CREATE_TABLE_STM = "CREATE TABLE IF NOT EXISTS ${TABELA_DISCIPLINA}("+
                "${ATRIBUTO_CODIGO} TEXT NOT NULL PRIMARY KEY, " +
                "${ATRIBUTO_NOME} TEXT NOT NULL, " +
                "${ATRIBUTO_EMENTA} TEXT NOT NULL);"
    }

    // Referencia do BD do app
    val sqlDb: SQLiteDatabase
    init {
        // Cria Banco
        sqlDb = contexto.openOrCreateDatabase(CURSO_BD, MODE_PRIVATE, null)

        // Cria a tabela
        try {
            sqlDb.execSQL(CREATE_TABLE_STM)
        } catch (e: SQLException) {
            Log.e(contexto.getString(R.string.app_name), "Erro na criacao da tabela")
        }
    }

    override fun createDisciplina(disciplina: Disciplina) {
        // Mapeando atributo-valor
        val atributos = ContentValues()
        atributos.put(ATRIBUTO_CODIGO, disciplina.codigo)
        atributos.put(ATRIBUTO_NOME, disciplina.nome)
        atributos.put(ATRIBUTO_EMENTA, disciplina.ementa)

        // Insere na tabela
        sqlDb.insert(TABELA_DISCIPLINA, null, atributos)
    }

    override fun readDisciplina(codigo: String): Disciplina {
        // Consulta com a funcao query
        val disciplinaCursor = sqlDb.query(
            true,
            TABELA_DISCIPLINA,
            null,
            "$ATRIBUTO_CODIGO = ?",
            arrayOf("$codigo"),
            null, null,
            null,
            null
        )
        // Retorna disciplina encontrada ou vazia
        return if (disciplinaCursor.moveToFirst())
                linhaCursorParaDisciplina(disciplinaCursor)
            else
                Disciplina()

    }

    /* Converte uma linha do Cursor para uma objeto de Disciplina */
    private fun linhaCursorParaDisciplina(cursor: Cursor): Disciplina {
        return Disciplina(cursor.getString(
            cursor.getColumnIndex(ATRIBUTO_CODIGO)),
            cursor.getString(cursor.getColumnIndex(ATRIBUTO_NOME)),
            cursor.getString(cursor.getColumnIndex(ATRIBUTO_EMENTA))
        )
    }

    override fun readDisciplinas(): MutableList<Disciplina> {
        val listaDisciplinas = mutableListOf<Disciplina>()

        val disciplinasStm = "SELECT * FROM disciplina;"
        val disciplinasCursor = sqlDb.rawQuery(disciplinasStm, null)

        while (disciplinasCursor.moveToNext()) {
            listaDisciplinas.add(linhaCursorParaDisciplina(disciplinasCursor))
        }
        return listaDisciplinas
    }

    override fun updateDisciplina(disciplina: Disciplina) {
        val atributos = ContentValues()
        atributos.put(ATRIBUTO_NOME, disciplina.nome)
        atributos.put(ATRIBUTO_EMENTA, disciplina.ementa)
        // Executa update
        sqlDb.update(TABELA_DISCIPLINA, atributos, "$ATRIBUTO_CODIGO = ?", arrayOf(disciplina.codigo))
    }

    override fun deleteDisciplina(codigo: String) {
        sqlDb.delete(TABELA_DISCIPLINA, "$ATRIBUTO_CODIGO = ?", arrayOf(codigo))
    }
}