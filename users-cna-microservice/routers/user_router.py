from typing import List
from fastapi import APIRouter, Depends
from pydantic import BaseModel

from db.dals.user_dal import UserDAL
from dependencies import get_user_dal  # you must have this in your app

router = APIRouter()

# ✅ Response schema
class UserOut(BaseModel):
    id: int
    name: str
    email: str
    mobile: int  # Because your SQLAlchemy model uses Integer

    class Config:
        from_attributes = True


# ✅ List all users
@router.get("/users", response_model=List[UserOut])
async def get_all_users(user_dal: UserDAL = Depends(get_user_dal)):
    return await user_dal.get_all_users()