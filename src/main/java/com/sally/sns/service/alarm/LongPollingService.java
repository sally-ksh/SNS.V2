package com.sally.sns.service.alarm;

import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("longPollingService")
public @interface LongPollingService {
}
