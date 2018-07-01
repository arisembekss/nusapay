package com.samimi.nusapay;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.fragments.FrPaket;
import com.samimi.nusapay.fragments.FrSingleNumber;
import com.samimi.nusapay.fragments.FrTagihan;
import com.samimi.nusapay.fragments.FrToken;
import com.samimi.nusapay.fragments.FrVgame;
import com.samimi.nusapay.fragments.TransactFragment;
import com.samimi.nusapay.preference.PrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.util.UIUtils;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class TransactActivity extends AppCompatActivity {

    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;
    PrefManager prefManager;

    Fragment fragment;
    FragmentManager fragmentManager;
    View first;
    TourGuide mTourGuideHandler;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transact);

        prefManager = new PrefManager(TransactActivity.this);
        fragment = new TransactFragment();
        //fragment.setRetainInstance(true);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentPulsa, fragment);

            fragmentTransaction.commit();
        }

        initUi();
        result = new DrawerBuilder()
                .withActivity(this)
                //.withToolbar(toolbar)
                //.withSliderBackgroundDrawable(getDrawable(R.drawable.bgd))
                //.withSliderBackgroundColorRes(R.color.blueA)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Halaman Transaksi").withIcon(GoogleMaterial.Icon.gmd_grain).withIdentifier(1).withSelectable(false),
                        //new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(GoogleMaterial.Icon.gmd_balance_wallet).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(GoogleMaterial.Icon.gmd_flash).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_voucher).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_paket).withIcon(GoogleMaterial.Icon.gmd_network_wifi).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(7),
                        //new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
                        //new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn"),
                        new DividerDrawerItem()
                        //new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        //new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem instanceof Nameable) {
                            //Toast.makeText(TransactActivity.this, ((Nameable) drawerItem).getName().getText(TransactActivity.this), Toast.LENGTH_SHORT).show();
                            String name = ((Nameable) drawerItem).getName().getText(TransactActivity.this);
                            //if (name.equals("Tagihan")) {
                                openFragment(name);
                            //}
                        }
                        return false;
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView();

        //the MiniDrawer is managed by the Drawer and we just get it to hook it into the Crossfader
        miniResult = result.getMiniDrawer();


        //get the widths in px for the first and second panel
        int firstWidth = (int) UIUtils.convertDpToPixel(300, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(72, this);

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
        crossFader = new Crossfader()
                //.withContent(findViewById(R.id.crossfade_content))
                .withContent(findViewById(R.id.fragmentPulsa))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_right);

        first = findViewById(R.id.viewFirst);
        if (prefManager.isTrFirstTimeLaunch()) {
            iniTour();
        }

    }

    private void initUi() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("server").child("status");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sstatus= String.valueOf(dataSnapshot.getValue());
                showSnakck(sstatus);
                Log.d("dbase real", sstatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSnakck(String sstatus) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snackstatus), "Server status: "+sstatus, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setTextColor(color);
        snackbar.show();
    }

    private void iniTour() {
        mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Selamat Datang di Halaman Transaksi!").setDescription(getResources().getString(R.string.desc_tr_first)))
                .setOverlay(new Overlay())
                .playOn(first);
    }

    private void openFragment(String name) {
        if (name.equals("Tagihan")) {

            fragment = new FrTagihan();
        } else if (name.equals("Pulsa")) {
            fragment = new FrSingleNumber();
        } else if (name.equals("Token")) {
            fragment = new FrToken();
        } else if (name.equals("Voucher Game")) {
            fragment = new FrVgame();
        } else if (name.equals("Paket Data")) {
            fragment = new FrPaket();
        } else if (name.equals("Home")) {
            super.onBackPressed();
        } else if (name.equals("Halaman Transaksi")) {
            crossFader.crossFade();
            if (prefManager.isTrFirstTimeLaunch()) {
                mTourGuideHandler.cleanUp();
                prefManager.setTrFirstTimeLaunch(false);
            }
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPulsa, fragment);
        fragmentTransaction.commit();

    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (crossFader != null && crossFader.isCrossFaded()) {
            crossFader.crossFade();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
