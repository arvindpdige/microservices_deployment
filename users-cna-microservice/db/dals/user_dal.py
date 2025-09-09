from typing import List, Optional
from sqlalchemy import update
from sqlalchemy.future import select
from sqlalchemy.ext.asyncio import AsyncSession

from db.models.user import User


class UserDAL:
    def __init__(self, db_session: AsyncSession):
        self.db_session = db_session

    async def create_user(self, name: str, email: str, mobile: int) -> User:
        new_user = User(name=name, email=email, mobile=mobile)
        self.db_session.add(new_user)
        await self.db_session.flush()
        return new_user

    async def get_all_users(self) -> List[User]:
        result = await self.db_session.execute(select(User).order_by(User.id))
        return result.scalars().all()

    async def get_user(self, user_id: int) -> Optional[User]:
        result = await self.db_session.execute(select(User).where(User.id == user_id))
        return result.scalar_one_or_none()

    async def update_user(
        self,
        user_id: int,
        name: Optional[str] = None,
        email: Optional[str] = None,
        mobile: Optional[int] = None,
    ) -> None:
        update_values = {}
        if name:
            update_values["name"] = name
        if email:
            update_values["email"] = email
        if mobile:
            update_values["mobile"] = mobile

        if update_values:
            stmt = (
                update(User)
                .where(User.id == user_id)
                .values(**update_values)
                .execution_options(synchronize_session="fetch")
            )
            await self.db_session.execute(stmt)
