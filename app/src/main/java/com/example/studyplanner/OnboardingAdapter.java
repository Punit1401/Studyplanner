package com.example.studyplanner;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder>{
    Context context;
    int[] images = {R.drawable.onboarding, R.drawable.onboarding};
    String[] titles = {"Welcome to Studify", "Explore Study Materials"};
    String[] descriptions = {"Plan your studies and stay organized.", "Access flashcards and explanations."};
    public OnboardingAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_onboarding,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(images[position]);
        holder.title.setText(titles[position]);
        holder.desc.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,desc;
        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}