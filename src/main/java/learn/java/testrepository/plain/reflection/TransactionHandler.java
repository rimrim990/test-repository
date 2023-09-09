package learn.java.testrepository.plain.reflection;

import jakarta.transaction.TransactionManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.TransactionStatus;

@RequiredArgsConstructor
public class TransactionHandler implements InvocationHandler {

    private final Object target;
    private final TransactionManager transactionManager;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final TransactionStatus status = (TransactionStatus) transactionManager.getTransaction();

        try {
            final Object result = method.invoke(target, args);
            transactionManager.commit();
            return result;
        } catch (InvocationTargetException ex) {
            transactionManager.rollback();
            // 타깃 메서드에서 발생한 예외를 랩핑하고 있음
            throw ex.getTargetException();
        }
    }
}
