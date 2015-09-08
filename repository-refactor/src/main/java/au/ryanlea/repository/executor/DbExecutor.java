package au.ryanlea.repository.executor;

import com.google.common.base.CaseFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class DbExecutor<T> implements Executor<T> {

    @Resource
    JdbcTemplate jdbcTemplate;


    @Override
    public Response<T> execute(final Request request) {
        Response<T> response = null;
        final StoredProc storedProc = request.getClass().getAnnotation(StoredProc.class);
        if(storedProc != null) {

            final SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.jdbcTemplate)
                    .withSchemaName(storedProc.schema())
                    .withCatalogName(storedProc.pkg())
                    .withProcedureName(storedProc.value());

            final SqlParameterSource parameters =  buildParameters(request);

            final Map<String, Object> output  = jdbcCall.execute(parameters);

            response = buildResponse(request, output);

        }
        return response;
    }

    public Response<T> buildResponse(final Request request, final Map<String, Object> output){
        Response<T> response = null;
        try {
            final Type[] responses = request.getClass().getGenericInterfaces();
            if (responses != null && responses.length > 0) {
                final Type[] types = ((ParameterizedType) responses[0]).getActualTypeArguments();
                if (types != null && types.length > 0) {
                    final Type type = types[0];
                    @SuppressWarnings("unchecked")
                    Response<T> uncheckedResponse = (Response<T>) Class.forName(type.getTypeName()).newInstance();
                    final List resultList  = (List)output.get("P_RESULTS");
                    final DbMapper<T> mapper = new DbMapper<>();
                    mapper.populate(uncheckedResponse, resultList);
                    response = uncheckedResponse;
                }
            }
        }catch(final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    private SqlParameterSource buildParameters(final Request request) {
        final MapSqlParameterSource inParams = new MapSqlParameterSource();
        try {
            for (final PropertyDescriptor propertyDescriptor :
                    Introspector.getBeanInfo(request.getClass()).getPropertyDescriptors()) {
                if (propertyDescriptor.getName().equalsIgnoreCase("class")) continue;
                final String propertyName = String.format("p_%s", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyDescriptor.getName()));
                if (propertyDescriptor.getReadMethod() != null) {
                    final Object value = propertyDescriptor.getReadMethod().invoke(request);
                    inParams.addValue(propertyName, value);
                }
            }
            return inParams;
        } catch(final IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return inParams;
    }
}