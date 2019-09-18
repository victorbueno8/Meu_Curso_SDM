package br.edu.ifsp.scl.sdm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.sdm.view.MainActivity
import br.edu.ifsp.scl.sdm.R
import br.edu.ifsp.scl.sdm.model.Disciplina

class DisciplinasAdapter(val activity: MainActivity,
                         val leiauteItem: Int,
                         val listaDisciplinas: MutableList<Disciplina>) :
                            ArrayAdapter<Disciplina>(activity, leiauteItem, listaDisciplinas){
    val layoutInflater: LayoutInflater
    init {
        layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val disciplina = listaDisciplinas.get(position)
        var view = convertView

        if (view == null) {
            view = layoutInflater.inflate(leiauteItem, parent, false)

            view.setTag(ItemHolder(
                view.findViewById(R.id.codigoDisciplinaTv),
                view.findViewById(R.id.nomeDisciplinaTv)
            ))
        }
        val itemHolder = view?.getTag() as ItemHolder
        itemHolder.codigoDisciplinaTv.text = disciplina.codigo
        itemHolder.nomeDisciplinaTv.text = disciplina.nome

        return view
    }

    private data class ItemHolder(
        val codigoDisciplinaTv: TextView,
        val nomeDisciplinaTv: TextView
    )
}