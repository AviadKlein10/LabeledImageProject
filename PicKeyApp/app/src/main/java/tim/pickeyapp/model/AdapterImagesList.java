package tim.pickeyapp.model;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import tim.pickeyapp.MainView;
import tim.pickeyapp.R;
import tim.pickeyapp.custom_view.AutoResizeTextView;
import tim.pickeyapp.custom_object.LabeledImage;
import tim.pickeyapp.custom_view.RoundedImageView;

/**
 * Created by Aviad on 28/05/2017.
 */

public class AdapterImagesList extends RecyclerView.Adapter<AdapterImagesList.ViewHolder> {
    private Context context;
    private ArrayList<LabeledImage> arrLabeledImages, fullArrLabeledImages;
    private int lastPosition = -1;
    private MainView mainView;
    private Typeface customFont;


    public AdapterImagesList(Context context, ArrayList<LabeledImage> arrLabeledImages, MainView mainView) {
        this.context = context;
        this.arrLabeledImages = arrLabeledImages;
        this.fullArrLabeledImages = arrLabeledImages;
        this.mainView = mainView;
        customFont = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans-Bold.ttf");
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        if (arrLabeledImages.get(i).getBitmapImage() != null) {
            // I know it's better to load image inside of recyclerView using library. But Glide, Picasso and UIL can't handle local bitmap
            holder.image.setImageBitmap(arrLabeledImages.get(i).getBitmapImage());
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
        }
        holder.txtLabels.setText(arrLabeledImages.get(i).getLabels());
        holder.txtDateCreated.setText(arrLabeledImages.get(i).getDateCreated());
        setAnimation(holder.itemView, getItemCount());

        holder.txtLabels.setTypeface(customFont);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.txtLabels.setTransitionName("txt_trans");
            holder.image.setTransitionName("img_trans");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (arrLabeledImages.get(position).getBitmapImage() != null) {
                    mainView.onImageItemClick(holder.getAdapterPosition(), arrLabeledImages.get(position).getLabels(), holder.txtLabels, holder.image, arrLabeledImages.get(position));
                }
            }

        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Remove Image when long pressed
                int position = holder.getAdapterPosition();
                arrLabeledImages.get(position).setBitmapImage(null);
                holder.image.setImageResource(R.drawable.ic_place_holder);
                arrLabeledImages.remove(position);
                notifyItemRemoved(position);
                return true;
            }
        });
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public AdapterImagesList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.labeld_image_cell, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (arrLabeledImages != null) {
            return arrLabeledImages.size();
        }
        return 0;
    }


    public void addLoadingItem() {
        arrLabeledImages.add(0, new LabeledImage(null, " "));
        notifyItemInserted(0);
    }

    // Replace loading item with new item;
    public void addNewItem(LabeledImage newLabledImage, int position) {
        arrLabeledImages.set(position - 1, newLabledImage);
        notifyItemChanged(position - 1);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        private AutoResizeTextView txtLabels;
        private ProgressBar progressBar;
        private TextView txtDateCreated;

        private ViewHolder(View view) {
            super(view);
            image = (RoundedImageView) view.findViewById(R.id.imgItem);
            txtLabels = (AutoResizeTextView) view.findViewById(R.id.titleItem);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            txtDateCreated = (TextView) view.findViewById(R.id.txtDate);
        }
    }


    public void performSearch(CharSequence charSequence) {
        if (charSequence.toString().trim().length() > 0) {
            arrLabeledImages = new ArrayList<>();
            for (int i = 0; i < fullArrLabeledImages.size(); i++) {
                String txtSearch = fullArrLabeledImages.get(i).getLabels().toLowerCase();
                if (txtSearch.contains(charSequence)) {
                    arrLabeledImages.add(fullArrLabeledImages.get(i));
                }
            }
            notifyDataSetChanged();
        }
    }

    public void initList() {
        this.arrLabeledImages = fullArrLabeledImages;
        notifyDataSetChanged();
    }
}
