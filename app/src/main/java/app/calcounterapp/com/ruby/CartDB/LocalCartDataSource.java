package app.calcounterapp.com.ruby.CartDB;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {

    private CartDAO cartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public Flowable<List<CartItem>> getAllCart(String useremail) {
        return cartDAO.getAllCart(useremail);
    }

    @Override
    public Single<Integer> getCartCount(String useremail) {
        return cartDAO.getCartCount(useremail);
    }

    @Override
    public Single<Long> cartSum(String useremail) {
        return cartDAO.cartSum(useremail);
    }

    @Override
    public Single<CartItem> cartItem(int id, String useremail) {
        return cartDAO.cartItem(id,useremail);
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCart(CartItem cartItem) {
        return cartDAO.updateCart(cartItem);
    }

    @Override
    public Single<Integer> deleteCart(CartItem cartItem) {
        return cartDAO.deleteCart(cartItem);
    }

    @Override
    public Single<Integer> cleanCart(String useremail) {
        return cartDAO.cleanCart(useremail);
    }
}
