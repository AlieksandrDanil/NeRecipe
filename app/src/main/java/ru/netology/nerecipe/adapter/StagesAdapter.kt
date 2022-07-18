package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.StageItemBinding
import ru.netology.nerecipe.dto.Stage

interface OnInteractionStageListener {
    fun onClicked(stage: Stage)
}

class StagesAdapter(
    private val onInteractionStageListener: OnInteractionStageListener,
) : ListAdapter<Stage, StageViewHolder>(StageDiffCallback()) { //RecyclerView.Adapter<StageViewHolder>() {

    private val pictureId = R.drawable.salat

    //val img = (Drawable) getResources().getDrawable(R.drawable.salat) as Drawable
    val idRecipe = 1L
    val listStage = listOf(
        Stage(0, idRecipe, 1, "Приготовление ингридиентов", "Берем тото...", pictureId),
        Stage(1, idRecipe, 2, "Нарезка", "Нарезаем так-то", pictureId),
        Stage(2, idRecipe, 3, "Варка", "Варим столько-то времени", pictureId),
        Stage(3, idRecipe, 4, "Промывка", "Под холодной водой промываем и охлаждаем", pictureId),
        Stage(4, idRecipe, 5, "Детальная нарезка", "Мелко нарезаем", pictureId),
        Stage(5, idRecipe, 6, "Приготовление соуса", "Смешиваем для приготовления...", pictureId),
        Stage(6, idRecipe, 7, "Укладываем", "Расскладываем по краям тарелки...", pictureId),
        Stage(7,
            idRecipe,
            8,
            "Заправка соусом",
            "Заправляем ингридиенты соусом следующим образом...",
            pictureId),
        Stage(7, idRecipe, 9, "Оформление и подача", "Подаем в охлажденном виде", pictureId)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StageItemBinding.inflate(inflater, parent, false)
        return StageViewHolder(binding, onInteractionStageListener)
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        val stage = getItem(position) // listStage[position] //
        holder.bind(stage)
    }
    //override fun getItemCount(): Int = listStage.size
}

class StageViewHolder(
    private val binding: StageItemBinding,
    private val onInteractionStageListener: OnInteractionStageListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(stage: Stage) {
        stageBinding(stage, binding, onInteractionStageListener)
    }
}

fun stageBinding(
    stage: Stage,
    binding: StageItemBinding,
    onInteractionStageListener: OnInteractionStageListener,
) {
    binding.apply {
        with(stageCardView) {
            tag = stage.pos
        }

        with(stageTextView) {
            text = "Шаг ${stage.pos}\n${stage.name}\n${stage.description}"

            setOnClickListener {
                stageImageView.visibility = View.VISIBLE
                onInteractionStageListener.onClicked(stage)
            }
        }

        with(stageImageView) {
            setImageResource(R.drawable.salat)
//            setOnClickListener {
//                visibility = View.GONE
//            }
        }
    }
}

class StageDiffCallback : DiffUtil.ItemCallback<Stage>() {
    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem == newItem
    }
}