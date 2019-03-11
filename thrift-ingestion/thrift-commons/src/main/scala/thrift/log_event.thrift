namespace * logschema

typedef string timestamp

enum DeviceType {
     WEBAPP,
     IOS,
     ANDROID,
     OTHER
}

enum LogType {
    DEBUG,
    INFO,
    WARNING,
    ERROR
}

struct LogEvent {
    1: i16 version,
    2: timestamp currentDate,
    3: timestamp startDate,
    4: timestamp endDate,
    5: string pollster,
    6: i32 favor,
    7: i32 oppose,
    8: i32 total,
    9: string url,
    10: DeviceType device,
    11: LogType message
}
service LogEventService {
    void pushLogEvent(1: LogEvent logEvent),
    LogEvent pullLogEvent()
}