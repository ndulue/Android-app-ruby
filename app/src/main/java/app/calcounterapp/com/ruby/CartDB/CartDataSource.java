package app.calcounterapp.com.ruby.CartDB;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {

    Flowable<List<CartItem>> getAllCart(String useremail);

    Single<Integer> getCartCount(String useremail);

    Single<Long> cartSum(String useremail);

    Single<CartItem> cartItem(int id,String useremail);

    Completable insertOrReplaceAll(CartItem...cartItems);

    Single<Integer> updateCart(CartItem cartItem);

    Single<Integer> deleteCart(CartItem cartItem);

    Single<Integer> cleanCart(String useremail);

}
