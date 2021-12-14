const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

const db = admin.firestore();

exports.updateUser = functions.firestore
  .document("matching/{matchId}")
  .onUpdate(async (change, context) => {
    // console.log(change.after.data())
    // console.log(change.before.data())
    // Get an object representing the document
    // e.g. {'name': 'Marie', 'age': 66}
    const newValue = change.after.data();

    // ...or the previous value before this update
    const previousValue = change.before.data();

    // access a particular field as you would any JS property
    const name = newValue.name;
    // const auth = auth.get;
    // const user = auth.currentUser;

    if (previousValue.users.length == 3) {
      if (newValue.users.length > 3) {
        const payload = {
          data: {
            title: "임시 팀 매칭",
            body: `임시 팀 매칭이 완료되었습니다. 수락과 거부로 의사를 표현해주세요`,
            clickAction: "MatchingActivity",
          },
        };

        const results = await Promise.all(
          newValue.users.map((user) => user.get())
        );
        const tokens = results.map((result) => result.data().fcm_token);

        admin
          .messaging()
          .sendToDevice(tokens, payload)
          .then(function (response) {
            console.log("Matching Table Successfully sent message:", response);
          })
          .catch(function (error) {
            console.log("Matching Table Error sending message:", error);
          });
      }
    } else if (previousValue.users.length == 4 && newValue.users.length == 4) {
      //수락 거부 해서 판단하는 상태 -> 4명의 상태가 다 wating인지 확인하고
      if (newValue.approvals.every((x) => x != "waiting")) {
        if (newValue.approvals.every((x) => x == "agree")) {
          //매칭 완료
          db.collection("matching")
            .doc(context.params.matchId)
            .update({ state: "matched" });

          const payload = {
            data: {
              title: "팀 매칭 완료",
              body: `팀 매칭이 완료되었습니다. 서로의 카카오톡 ID를 확인하고 서로 연락하세요`,
              clickAction: "TeamActivity",
            },
          };

          const results = await Promise.all(
            newValue.users.map((user) => user.get())
          );
          const tokens = results.map((result) => result.data().fcm_token);

          console.log(tokens);

          admin
            .messaging()
            .sendToDevice(tokens, payload)
            .then(function (response) {
              console.log(
                "Matching Table Successfully sent message:",
                response
              );
            })
            .catch(function (error) {
              console.log("Matching Table Error sending message:", error);
            });
        } else {
          //매칭 실패
          const disagrees = newValue.approvals
            .map((approval, index) => {
              return {
                index: index,
                approval: approval,
              };
            })
            .filter((x) => x.approval == "disagree");
          const disagreeUsers = disagrees.map(
            (disagree) => newValue.users[disagree.index]
          );

          return change.after.ref.update({
            users: admin.firestore.FieldValue.arrayRemove(...disagreeUsers),
            approvals: admin.firestore.FieldValue.arrayRemove("disagree"),
          });
        }
      }
    }
  });
