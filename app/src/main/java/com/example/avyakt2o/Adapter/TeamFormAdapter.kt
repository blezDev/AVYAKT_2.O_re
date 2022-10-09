package com.example.avyakt2o.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.avyakt2o.R

class TeamFormAdapter(private val TeamList: ArrayList<com.example.avyakt2o.data.TeamForm>,val context: Context):RecyclerView.Adapter<TeamFormAdapter.TeamViewHolder>()  {
    var onItemClick : ((com.example.avyakt2o.data.TeamForm) -> Unit) ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.form_model,parent,false)
        return TeamViewHolder(v)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {

        val Team = TeamList[position]
        holder.Name.hint = Team.name
        holder.Roll.hint = Team.roll
        holder.Email.hint = Team.mail
        holder.teamName.hint = Team.teamName
        holder.Phone.hint = Team.phone
        holder.btn_Verify.setOnClickListener {
            val name = holder.Name.text.toString()
            val email = holder.Email.text.toString()
            val roll = holder.Roll.text.toString()
            val teamname = holder.teamName.text.toString()
            val phone = holder.Phone.text.toString()


            if(name.isNotEmpty() && email.isNotEmpty() && roll.isNotEmpty() && teamname.isNotEmpty() && phone.isNotEmpty())
            {

                onItemClick?.invoke(com.example.avyakt2o.data.TeamForm(
                    name = name,
                    mail = email,
                    roll = roll,
                    teamName = teamname,
                    phone = phone

                ))
                holder.apply {
                   Name.isEnabled = false
                    Roll.isEnabled = false
                    Email.isEnabled = false
                    teamName.isEnabled = false
                    Phone.isEnabled = false
                }

                holder.btn_Verify.apply {
                  text = "ADDED  ✅"
                    isClickable = false
                }

            }

            else if (name.isNotEmpty() && email.isNotEmpty() && roll.isNotEmpty() && teamname.isEmpty() && phone.isNotEmpty()){
                onItemClick?.invoke(com.example.avyakt2o.data.TeamForm(
                    name = name,
                    mail = email,
                    roll = roll,
                    teamName = "",
                    phone = phone

                ))
                holder.apply {
                    Name.isEnabled = false
                    Roll.isEnabled = false
                    Email.isEnabled = false
                    teamName.isEnabled = false
                    Phone.isEnabled = false
                }

                holder.btn_Verify.apply {
                    setText("ADDED  ✅")
                    isClickable = false
                }

            }

            else{
                SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("All Fields must not be empty")
                    .show()
            }

        }

    }

    override fun getItemCount(): Int {
        return TeamList.size
    }


    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Name: EditText = itemView.findViewById(R.id.memberName)
        val Roll: EditText = itemView.findViewById(R.id.memberRoll)
        val Email: EditText = itemView.findViewById(R.id.memberEmail)
        val teamName: EditText = itemView.findViewById(R.id.teamName)
        val Phone: EditText = itemView.findViewById(R.id.memberPhone)
        val btn_Verify : Button = itemView.findViewById(R.id.btn_Verify)
    }
}