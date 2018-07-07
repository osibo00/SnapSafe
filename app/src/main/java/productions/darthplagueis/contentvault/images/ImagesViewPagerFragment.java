package productions.darthplagueis.contentvault.images;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.contentvault.R;
import productions.darthplagueis.contentvault.databinding.ImagesVpFragmentBinding;
import productions.darthplagueis.contentvault.images.view.ImagesFragment;

public class ImagesViewPagerFragment extends Fragment {

    private ImagesVpFragmentBinding vpFragmentBinding;


    public ImagesViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vpFragmentBinding = ImagesVpFragmentBinding.inflate(inflater, container, false);
        return vpFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ImagesFragment());
        fragmentList.add(new BlankFragment2());
        ImagesPagerAdapter imagesPagerAdapter = new ImagesPagerAdapter(getChildFragmentManager(), fragmentList);
        vpFragmentBinding.imagesViewPager.setAdapter(imagesPagerAdapter);
    }
}
