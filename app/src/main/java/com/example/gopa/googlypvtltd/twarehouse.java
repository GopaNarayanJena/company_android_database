package com.example.gopa.googlypvtltd;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gopa.googlypvtltd.R;

import java.util.ArrayList;
import java.util.List;

public class twarehouse extends Fragment {
    List<String> userSelection=null;
    ListView lv;
    String tablename;
    View row;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        row= inflater.inflate(R.layout.twarehouse,container,false);
        return row;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Warehouse");
        fetch();
        lv=view.findViewById(R.id.profile);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(modeListener);
    }

    public void fetch(){
        tablename = (String) getActivity().getTitle();
        String type = "select";
        constants.progressDialog = new ProgressDialog(getContext());
        constants.progressDialog.setTitle("loading");
        constants.progressDialog.setMessage("please wait....");
        constants.progressDialog.show();
        constants.progressDialog.setCancelable(false);
        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext(),row);
        backgroundWorker.execute(type, tablename);
    }

    AbsListView.MultiChoiceModeListener modeListener=new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(userSelection.contains(constants.warehouseId.get(position))){
                userSelection.remove(constants.warehouseId.get(position));
            }else{
                userSelection.add(constants.warehouseId.get(position));
            }
            View v=lv.getChildAt(position);
            ImageView tx=v.findViewById(R.id.chck);
            if(tx.getVisibility()==View.VISIBLE){
                tx.setVisibility(View.INVISIBLE);
            }
            else{
                tx.setVisibility(View.VISIBLE);
            }
            mode.setTitle(userSelection.size()+" selected");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            constants.menuInflater.inflate(R.menu.main,menu);
            userSelection=new ArrayList<>();
            constants.actionBar.hide();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int count=userSelection.size();
            switch (item.getItemId()){
                case R.id.delete:
                    for (int i=0;i<count;i++) {
                        String cust=userSelection.get(i);
                        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext(), row);
                        backgroundWorker.execute("delete", tablename, cust);
                    }
                    mode.finish();
                    fetch();
                    return true;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            constants.actionBar.show();
            int count=constants.warehouseId.size();
            for(int i=0;i<count;i++){
                View v=lv.getChildAt(i);
                ImageView ix=v.findViewById(R.id.chck);
                if(ix.getVisibility()==View.VISIBLE){
                    ix.setVisibility(View.INVISIBLE);
                }
            }
            userSelection=null;
        }
    };

}