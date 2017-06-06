package com.opencharge.opencharge.presentation.fragments;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.opencharge.opencharge.R;
import com.opencharge.opencharge.domain.Entities.Point;
import com.opencharge.opencharge.domain.Entities.User;
import com.opencharge.opencharge.domain.Entities.UserPointSummary;
import com.opencharge.opencharge.domain.use_cases.DeleteUserUseCase;
import com.opencharge.opencharge.domain.use_cases.GetCurrentUserUseCase;
import com.opencharge.opencharge.domain.use_cases.PointByIdUseCase;
import com.opencharge.opencharge.domain.use_cases.UserByIdUseCase;
import com.opencharge.opencharge.presentation.activities.NavigationActivity;
import com.opencharge.opencharge.presentation.activities.SignInActivity;
import com.opencharge.opencharge.presentation.adapters.CustomUserPointsAdapter;
import com.opencharge.opencharge.presentation.adapters.PointsAdapter;
import com.opencharge.opencharge.presentation.locators.UseCasesLocator;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DmnT on 17/05/2017.
 */

public class UserInfoFragment extends Fragment {

    private PointsAdapter pointsAdapter;
    private ImageView imatgeUsuari;
    private TextView nomUsuari;
    private TextView emailUsuari;
    private TextView minutsUsuari;
    private ListView puntsUsuari;
    private Button botoEliminarCompta;

    private static final String ARG_USER_ID = "user_id";
    private String userId;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId String
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String userId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    public static UserInfoFragment newInstance() {
        UserInfoFragment fragment = new UserInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        imatgeUsuari = (ImageView) view.findViewById(R.id.perfil_usuari_imatge);
        nomUsuari = (TextView) view.findViewById(R.id.perfil_usuari_nom2);
        emailUsuari = (TextView) view.findViewById(R.id.perfil_usuari_email2);
        minutsUsuari = (TextView) view.findViewById(R.id.perfil_usuari_minuts2);
        puntsUsuari = (ListView) view.findViewById(R.id.perfil_usuari_punts2);
        botoEliminarCompta = (Button) view.findViewById(R.id.perfil_usuari_boto_eliminar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UseCasesLocator useCasesLocator = UseCasesLocator.getInstance();
        if (needsToShowCurrentUser()) {

            Context context = getActivity();
            GetCurrentUserUseCase getCurrentUserUseCase = useCasesLocator.getGetCurrentUserUseCase(context, new GetCurrentUserUseCase.Callback() {
                @Override
                public void onCurrentUserRetrieved(User currentUser) {
                    setUpViewForUser(currentUser);
                }
            });
            getCurrentUserUseCase.execute();

        }
        else {

            UserByIdUseCase userByIdUseCase = useCasesLocator.getUserByIdUseCase(new UserByIdUseCase.Callback() {
                @Override
                public void onUserRetrieved(User user) {
                    setUpViewForUser(user);
                }
            });
            userByIdUseCase.setUserId(userId);
            userByIdUseCase.execute();

        }

    }

    private boolean needsToShowCurrentUser() {
        return (userId == null);
    }

    private void setUpViewForUser(final User user) {
        Picasso.with(getActivity().getApplicationContext()).load(user.getPhoto()).into(imatgeUsuari);
        nomUsuari.setText(user.getUsername());
        emailUsuari.setText(user.getEmail());
        minutsUsuari.setText(user.getMinutes().toString());
        //possible fallo que el context no s'estigui passant correctament?:


        puntsUsuari.setAdapter(new CustomUserPointsAdapter(getActivity().getApplicationContext(), user.getPoints()));

        puntsUsuari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId) {
                String pointId = user.getPoints().get(itemPosition).getPointId();
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    PointInfoFragment fragment = PointInfoFragment.newInstance(pointId);
                    ft.replace(R.id.content_frame, fragment).commit();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        botoEliminarCompta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Segur?");
                alertDialog.setMessage("Es perdràn tots els punts, minuts i reserves. Aquesta acció no es pot desfer.");

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO aqui s'ha de cridar la funció que esborri l'usuari
                        final UseCasesLocator useCasesLocator = UseCasesLocator.getInstance();
                        GetCurrentUserUseCase getCreateUsersUseCase = useCasesLocator.getGetCurrentUserUseCase(getActivity(), new GetCurrentUserUseCase.Callback() {
                            @Override
                            public void onCurrentUserRetrieved(User currentUser) {
                                List<UserPointSummary> points = currentUser.getPoints();
                                for (UserPointSummary point : points) {
                                    FirebaseDatabase.getInstance().getReference("Points").child(point.getPointId()).removeValue();
                                    /*PointByIdUseCase pointByIdUseCase = useCasesLocator.getPointByIdUseCase(new PointByIdUseCase.Callback() {
                                        @Override
                                        public void onPointRetrieved(Point point) {
                                            if (point.getAccessType() == Point.PARTICULAR_ACCESS)
                                                FirebaseDatabase.getInstance().getReference("Points").child(point.getId()).removeValue();
                                        }
                                    });
                                    pointByIdUseCase.setPointId(point.getPointId());
                                    pointByIdUseCase.execute();*/
                                }

                                FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getId()).removeValue();
                                //signOut();
                            }
                        });
                        getCreateUsersUseCase.execute();
                        // això és un placeholder per així tenir una resposta, un cop
                        // implementat correctament es pot deixar, o treure
                        //Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Usuari eliminat", Toast.LENGTH_SHORT);
                        //toast.show();
                    }
                });

                alertDialog.show();
            }

        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        //TODO: s'ha de mirar perque no es poden borrar les credencials de Google!!!
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Delete", "User account deleted.");
                        }
                    }
                });
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }
}
