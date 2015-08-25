package com.example.adamyaziji.reminderapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by adamyaziji on 19/04/2015.
 */
public class MemoList extends Fragment {
    Button addMemo, refreshList, deleteAllBtn;
    ListView memoList;
    TextView notice;
    Context context;
    CreateMemo memo;
    String memoToDelete;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.memo_list, container, false);
        context = container.getContext();
        memo = new CreateMemo();

        addMemo = (Button) view.findViewById(R.id.addMemoButton);
        memoList = (ListView) view.findViewById(R.id.memoListView);
        refreshList = (Button) view.findViewById(R.id.refreshButton);

        if(memo.load==true){
            populateListView();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Please refresh memos",
                    Toast.LENGTH_SHORT).show();
        }

        addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment add = new CreateMemo();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, add);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        refreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                populateListView();
            }
        });

        deleteAllBtn = (Button) view.findViewById(R.id.deleteAllButton);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
                aDialog.setTitle("Delete Memo");
                aDialog.setMessage("Are you sure?");
                aDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        memoToDelete = memo.getTaskName.toString();
                        delete();
                    }
                });
                aDialog.setNegativeButton("NO", null);
                aDialog.show();

            }
        });

        return view;
    }

    private void populateListView() {

        Cursor c = DataHandlerAdapter.dataHandler.getallRows();

        String[] fromMemo = new String[]{DataHandlerAdapter.dataHandler.MEMO_NAME,
                DataHandlerAdapter.DataHandler.MEMO_DATE, DataHandlerAdapter.DataHandler.MEMO_URGENCY};
        int[] itemViewId = new int[]{R.id.item_name, R.id.item_date, R.id.item_urgency};


        getActivity().startManagingCursor(c);

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter
                (context, R.layout.item_list, c, fromMemo, itemViewId);


        memoList.setAdapter(myCursorAdapter);
        Log.d("DataBase", "Data should show");

    }

    public void delete() {
        int count = DataHandlerAdapter.dataHandler.deleteRow();
        String countString = String.valueOf(count);


    }

    public void deleteAll() {
        DataHandlerAdapter.dataHandler.deleteAllDB();
    }
}

