package ru.nifontbus.notesbook.gui;

import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.nifontbus.notesbook.R;
import ru.nifontbus.notesbook.data.CardData;
import ru.nifontbus.notesbook.data.CardsSource;
import ru.nifontbus.notesbook.fragmentSendDataListener;

public class CardAdapter
        extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final static String TAG = "Card Adapter";
    private CardsSource dataSource;
    private final Fragment fragment;
    private fragmentSendDataListener itemClickListener;
    private int menuPosition;

    // Передаём в конструктор источник данных
    // В нашем случае это массив, но может быть и запрос к БД
    public CardAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDataSource(CardsSource dataSource){
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    // Создать новый элемент пользовательского интерфейса
    // Запускается менеджером
    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Создаём новый элемент пользовательского интерфейса
        // через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Заменить данные в пользовательском интерфейсе
    // Вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder viewHolder, int position) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран используя ViewHolder
        viewHolder.setData(dataSource.getCardData(position));
        CardData cardData = dataSource.getCardData(position);

        viewHolder.title.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.showDetailNote(cardData);
            }
        });

        Log.d(TAG, "onBindViewHolder POS = " + position);
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(fragmentSendDataListener onFragmentSendDataListener) {
        itemClickListener = onFragmentSendDataListener;
    }


    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на
    // один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private AppCompatImageView image;
        private CheckBox like;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            like = itemView.findViewById(R.id.like);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.list_item_img);

            registerContextMenu(itemView);

            // Обработчики нажатий на заголовке
            image.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10, 10);
                return true;
            });

            title.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10, 10);
                return true;
            });

        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });

                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(CardData cardData) {
            title.setText(cardData.getTitle());

            String str = cardData.getDescription();
            str = (str.length() < 60) ? str : str.substring(0, 59) + "...";
            description.setText(str);
            date.setText(new SimpleDateFormat("dd-MM-yy").format(cardData.getDate()));
            image.setImageResource(cardData.getPicture());
            image.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.showDetailNote(cardData);
                }
            });
        }
    }

    public int getMenuPosition() {
        return menuPosition;
    }

}
