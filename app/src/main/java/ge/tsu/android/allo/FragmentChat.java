package ge.tsu.android.allo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import ge.tsu.android.allo.data.Storage;
import ge.tsu.android.allo.data.StorageImp;

public class FragmentChat extends Fragment {
    public static String MESSAGE = "ge.tsu.allo.MESSAGE";
    public static String MESSAGE_DATA = "data";
    public static String data;
    private ListView listView;
    private FragmentChatAdapter fragmentChatAdapter;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_fragment_chat, container, false);
        listView=view.findViewById(R.id.list);
        listView.setAdapter(fragmentChatAdapter);
        fragmentChatAdapter=new FragmentChatAdapter(getContext(), 0, new ArrayList<AllMessages>());
          if(storage.getMessage( "sms", AllMessages[].class)!=null) {
            AllMessages[] allMessages= (AllMessages[]) storage.getMessage( "sms", AllMessages[].class);
            fragmentChatAdapter.addAll(allMessages);
        }
        editText=view.findViewById(R.id.text);
        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String text=editText.getText().toString();
                    Intent intent = new Intent();
                    intent.setAction(MESSAGE);
                    intent.putExtra(MESSAGE_DATA, text);
                    getActivity().sendBroadcast(intent);
                    setMessage();

            }
        });
        initBroadcast();
        return view;
    }
    public void initBroadcast() {
        FragmentChatCatcherBroadcast fragmentChatCatcherBroadcast = new FragmentChatCatcherBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE);
        getActivity().registerReceiver(fragmentChatCatcherBroadcast, intentFilter);

    }
    public void setMessage(){
        Storage storage = new StorageImp(getContext());
        ArrayList<AllMessages> messages = new ArrayList<>();
        AllMessages message=new AllMessages();
        message.setText(data);
        if(this.getId()==R.id.zezvasChat){
            message.setSenderName("Zezva");
        }
        else {
            message.setSenderName("Mzia");
        }
         Object object=storage.getMessage( "sms", ArrayList.class);
        if (object!=null) {
            messages = (ArrayList<AllMessages>)object;
            messages.add(message);
            storage.addMessage("sms", messages);


        }
        else {
            messages.add(message);
            storage.addMessage( "sms", messages);


        }
        if(storage.getMessage( "sms", AllMessages[].class)!=null) {
            AllMessages[] allMessages= (AllMessages[]) storage.getMessage( "sms", AllMessages[].class);
            fragmentChatAdapter.addAll(allMessages);
        }
    }

    public static class FragmentChatCatcherBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(MESSAGE_DATA)){
                 data=intent.getStringExtra(MESSAGE_DATA);
                Toast.makeText(context, "message sent", Toast.LENGTH_SHORT).show();


            }
        }
    }
}

class FragmentChatAdapter extends ArrayAdapter<AllMessages> {
    private Context mContext;

    public FragmentChatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AllMessages> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AllMessages current = getItem(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_fragment_chat_item, parent, false);
        TextView textView=view.findViewById(R.id.sendersName);
        textView.setText(current.getSenderName());
        TextView textView1=view.findViewById(R.id.sendersSms);
        textView1.setText(current.getText());
        return view;
    }


}

