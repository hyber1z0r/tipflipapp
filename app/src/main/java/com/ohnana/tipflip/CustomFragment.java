package com.ohnana.tipflip;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class CustomFragment extends Fragment {

    protected View rootView;

    protected abstract boolean canGoBack();


    @Override
    public void onResume() {
        super.onResume();
    }

    public View createView(LayoutInflater inflater, ViewGroup container, int layout) {
        if(rootView==null) {
            rootView = inflater.inflate(layout, container, false);
        } else {
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String action);
    }
}
