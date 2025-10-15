package code.codeapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCodeMember is a Querydsl query type for CodeMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeMember extends EntityPathBase<CodeMember> {

    private static final long serialVersionUID = -1417403633L;

    public static final QCodeMember codeMember = new QCodeMember("codeMember");

    public final StringPath email = createString("email");

    public final ListPath<MemberRole, EnumPath<MemberRole>> memberRoleList = this.<MemberRole, EnumPath<MemberRole>>createList("memberRoleList", MemberRole.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath pw = createString("pw");

    public final BooleanPath social = createBoolean("social");

    public QCodeMember(String variable) {
        super(CodeMember.class, forVariable(variable));
    }

    public QCodeMember(Path<? extends CodeMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeMember(PathMetadata metadata) {
        super(CodeMember.class, metadata);
    }

}

