package com.dix.base.controller;

import com.dix.base.common.CoreConfig;
import com.dix.base.common.ErrorResponse;
import com.dix.base.exception.BaseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, Throwable.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse defaultErrorHandler(HttpServletRequest request, Exception e, Throwable ex) {
        ErrorResponse errorResponse = new ErrorResponse(BaseException.ERROR_IN_INTERCEPTOR, "an error occurred");

        if (ex != null)
        {
            if (CoreConfig.getInstance().getDebug().get())
            {
                errorResponse.setMessage(ExceptionUtils.getStackTrace(ex));
            }
            else
            {
                errorResponse.setMessage(ex.getMessage());
            }

            ex.printStackTrace();
        }

        if (e != null)
        {
            if (CoreConfig.getInstance().getDebug().get())
            {
                errorResponse.setMessage(ExceptionUtils.getStackTrace(e));
            }
            else
            {
                errorResponse.setMessage(e.getMessage());
            }

            if (e instanceof MissingServletRequestParameterException)
            {
                MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) e;
                errorResponse.setCode(BaseException.ERROR_PARAM_NOT_SET);
                errorResponse.setMessage(String.format("%s not set", missingServletRequestParameterException.getParameterName()));
            }

            if (e instanceof BaseException)
            {
                BaseException baseException = (BaseException)e;
                errorResponse.setCode(baseException.getCode());
                errorResponse.setMessage(baseException.getMessage());
            }

            e.printStackTrace();
        }

        return errorResponse;
    }
}