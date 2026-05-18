import os
from flask import Flask, request, jsonify, render_template
import re
import requests
import ollama
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import DeclarativeBase, MappedAsDataclass
from sqlalchemy.orm import Mapped, mapped_column, relationship


app = Flask(__name__)

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "gemma4:e2b" 

class Base(DeclarativeBase, MappedAsDataclass):
    pass


db = SQLAlchemy(model_class=Base)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///backend.db"
db.init_app(app)



class User(Base):
    __tablename__ = "user"
    id: Mapped[int] = mapped_column(primary_key=True, init=False)
    name: Mapped[str] = mapped_column(unique=True)
    total_questions: Mapped[int]
    correct_questions: Mapped[int]    
    incorrect_questions: Mapped[int]


with app.app_context():
    db.create_all()
    db.session.commit()



def fetch_quiz_from_ollama(student_topic):
    print("Fetching quiz from Ollama")

    prompt = (
        f"Generate a quiz with 3 questions to test students on the provided topic.\n"
        f"For each question, generate 4 options where only one is correct.\n\n"
        f"Format exactly like this:\n"
        f"QUESTION: [Your question here]?\n"
        f"OPTION 0: [First option]\n"
        f"OPTION 1: [Second option]\n"
        f"OPTION 2: [Third option]\n"
        f"OPTION 3: [Fourth option]\n"
        f"ANS: [Correct answer option number]\n\n"
        f"Repeat for all questions.\n\n"
        f"Topic: {student_topic}"
    )

    response = ollama.chat(model=MODEL_NAME, messages=[
        {
            'role': 'user',
            'content': prompt,
        },
    ])

    print(response.message.content)

    


    return response.message.content


def process_quiz(quiz_text):
    questions = []

    pattern = re.compile(
        r'QUESTION: (.+?)\nOPTION 0: (.+?)\nOPTION 1: (.+?)\nOPTION 2: (.+?)\nOPTION 3: (.+?)\nANS: (.+?)(?:\n|$)',
        re.DOTALL
    )

    matches = pattern.findall(quiz_text)

    for match in matches:
        question = match[0].strip()
        options = [match[1].strip(), match[2].strip(), match[3].strip(), match[4].strip()]
        correct_ans = int(match[5].strip())

        question_data = {
            "question": question,
            "options": options,
            "correct_answer": correct_ans
        }
        questions.append(question_data)

    return questions


@app.route('/getQuiz', methods=['GET'])
def get_quiz():
    print("Request received")
    student_topic = request.args.get('topic')

    if student_topic is None:
        return jsonify({'error': 'Missing topic parameter'}), 400

    quiz = fetch_quiz_from_ollama(student_topic)
    return jsonify({'quiz': process_quiz(quiz)}), 200


@app.route("/getHint")
def get_hint():
    print("Generating hint")
    question = request.args.get("question")
    correct_answer = request.args.get("answer")

    prompt = f"question is " + question + " answer is " + correct_answer + " generate a hint for the user without giving them the answer"

    response = ollama.chat(model=MODEL_NAME, messages=[
        {
            'role': 'user',
            'content': prompt,
        },
    ])

    print(response.message.content)

    return jsonify({"content": response.message.content, "prompt": prompt }), 200


@app.route("/explainAnswer")
def get_explanation():
    print("Generating explanation")
    question = request.args.get("question")
    correct_answer = request.args.get("correct")
    incorrect_answer = request.args.get("incorrect")

    prompt = f"question is " + question + " correct answer is " + correct_answer + " incorrect answer is " + incorrect_answer + " explain why in 200 words or less. User no markdown only text"

    response = ollama.chat(model=MODEL_NAME, messages=[
        {
            'role': 'user',
            'content': prompt,
        },
    ])

    print(response.message.content)

    return jsonify({"content": response.message.content, "prompt": prompt }), 200


@app.route('/test', methods=['GET'])
def run_test():
    return jsonify({'quiz': "test"}), 200

@app.route("/addUserData", methods=['POST'])
def addUserData():
    name = request.form.get("name")
    id = request.form.get("id")
    correct_questions = request.form.get("correct_questions")
    incorrect_questions = request.form.get("incorrect_questions")
    total_questions = request.form.get("total_questions")

    user = User(id=id, name=name, correct_questions=correct_questions, incorrect_questions=incorrect_questions, total_questions=total_questions)
    db.session.add(user)
    db.session.commit()

    pass

@app.route("/user", methods=['GET'])
def viewUser():
    query = request.args.get('id')
    user = User(name="oliver", total_questions=10, correct_questions=7, incorrect_questions=3)
    
    db.select(User).filter_by(id=id)
    user = db.session.execute(query).scalar_one_or_none()

    return render_template("user.html", user=user)

if __name__ == '__main__':
    port_num = 5000
    print(f"App running on port {port_num}")
    app.run(port=port_num, host="0.0.0.0")