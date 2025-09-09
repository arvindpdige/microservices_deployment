FROM python:3.10.5-alpine3.16
RUN pip install pipenv
WORKDIR /usr/src/app
COPY Pipfile ./
RUN pipenv install
EXPOSE 9090
# copy the local app/ folder to the /app fodler in the container
COPY . .
# execute the command python main.py (in the WORKDIR) to start the app
CMD ["pipenv", "run", "python", "app.py"]
