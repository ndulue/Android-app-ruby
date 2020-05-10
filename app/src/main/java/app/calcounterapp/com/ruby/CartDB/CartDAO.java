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

public interface CartDAO {

    @Query("SELECT * FROM Cart WHERE useremail=:useremail")
    Flowable<List<CartItem>> getAllCart(String useremail);

    @Query("SELECT COUNT(*) FROM Cart WHERE useremail=:useremail")
    Single<Integer> getCartCount(String useremail);

    @Query("SELECT SUM(quantity * price) FROM Cart WHERE useremail=:useremail")
    Single<Long> cartSum(String useremail);

    @Query("SELECT * FROM Cart WHERE id=:id AND useremail=:useremail")
    Single<CartItem> cartItem(int id,String useremail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem...cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateCart(CartItem cartItem);

    @Delete
    Single<Integer> deleteCart(CartItem cartItem);

    @Query("DELETE FROM Cart WHERE useremail=:useremail")
    Single<Integer> cleanCart(String useremail);
}
