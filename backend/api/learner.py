from api.models import PreviousInfo
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense, Dropout
import numpy as np
def learn(app_user, start_date, end_date):
    tmp = PreviousInfo.objects.filter(appUser= app_user, date= start_date)

    data = np.array([[i, i] for i in range(1, 366)])  # 날짜와 판매량
    timesteps = 10  # 입력 시퀀스 길이

    def create_sequences(data, sequence_length):
        X, y = [], []
        for i in range(len(data) - sequence_length):
            X.append(data[i:i + sequence_length])  # 날짜와 판매량 포함한 입력
            y.append(data[i + sequence_length, 1])  # 판매량만 예측 대상
        return np.array(X), np.array(y)

    X, y = create_sequences(data, timesteps)

    # 데이터 차원 변경 (LSTM 입력 형태: samples, timesteps, features)
    X = X.reshape((X.shape[0], 10, 2))  # (samples, timesteps, features)

    x_train = X[:250]
    y_train = y[:250]
    x_val = X[250:320]
    y_val = y[250:320]
    x_test = X[320:]
    y_test = y[320:]

    